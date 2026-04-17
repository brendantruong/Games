<script lang="ts">
	import { QuestionState, type Question } from '$lib';
	import type { Socket } from 'socket.io-client';
	import Tooltip from './Tooltip.svelte';
	// This component is used to display a question card when a question is selected.

	// whether or not SOMEONE has buzzed in.
	let buzzed = $state(false);
	// tries to set buzzed to true. otherwise sets buzzed to false.
	let setBuzzed = (value: boolean) => {
		value ? socket.emit('buzzed', name) : ((whoBuzzed = ''), (buzzed = false));
	};

	let whoBuzzed = $state('');
	let guess = $state('');
	let {
		selectedQuestion,
		socket,
		name,
		backToBoard
	}: {
		selectedQuestion: Question;
		socket: typeof Socket;
		name: string;
		backToBoard: () => void;
	} = $props();
	let userAnswer = $state('');
	let questionState = $derived(() =>
		selectedQuestion.answered
			? QuestionState.ShowAnswer
			: buzzed
				? QuestionState.Guessing
				: QuestionState.Open
	);
	let secondsLeft = $state(0);
	let isAnswering = $derived(whoBuzzed === name && questionState() === QuestionState.Guessing);
	let answeringInterval = $state<NodeJS.Timeout>();
	let activeQuestionKey = $state<string>(); // to track which question the timer belongs to
	const setTimer = (seconds: number) => {
		activeQuestionKey = selectedQuestion.question; // set the active question key
		isAnswering = true;
		secondsLeft = seconds;
		answeringInterval = setInterval(() => {
			console.log(
				'Seconds left for',
				name,
				':',
				secondsLeft,
				'for question:',
				selectedQuestion.question
			);
			if (secondsLeft > 0) {
				secondsLeft -= 1;
			} else {
				// time's up, move to show answer
				secondsLeft = 0;
				isAnswering = false;
				clearInterval(answeringInterval); // stop the timer
				console.log(answeringInterval, 'cleared');
				answeringInterval = undefined;
				// only submit if this timer belongs to the currently active question
				if (activeQuestionKey === selectedQuestion.question) {
					submitAnswer(userAnswer);
				} else {
					console.log('Ignored stale timer for:', activeQuestionKey);
				}
			}
		}, 1000);
	};

	// SOCKET LISTEN EVENTS
	onMount(() => {
		const handleBuzzed = (playerName: string) => {
			whoBuzzed = playerName;
			if (playerName) {
				console.log(`${playerName} buzzed in!`);
				buzzed = true;
				if (playerName === name) {
					setTimer(5); // start timer only for the player who buzzed in
				}
			} else {
				console.log('reset buzzer');
				buzzed = false;
			}
		}

		const handleCheckAnswer = (buzzGuess: string, socketId: string) => {
			console.log(
				`Guess: ${buzzGuess} for question: ${selectedQuestion.question} by ${socketId}`
			);
			guess = buzzGuess;
		}

		const handleTimeUp = () => {
			new Audio('https://www.myinstants.com/media/sounds/times-up.mp3').play();
		}

		socket.on('buzzed', handleBuzzed);
		socket.on('checkAnswer', handleCheckAnswer);
		socket.on('timeUp', handleTimeUp);

		onDestroy(() => {
			socket.off('buzzed', handleBuzzed);
			socket.off('checkAnswer', handleCheckAnswer);
			socket.off('timeUp', handleTimeUp);

			if (answeringInterval) {
				clearInterval(answeringInterval);
				answeringInterval = undefined;
			}
		});
	});


	import { scale } from 'svelte/transition';
	import { onDestroy, onMount } from 'svelte';
	const isCorrect = $derived(selectedQuestion.answer.toLowerCase() === guess.toLowerCase().trim());
	function submitAnswer(answer: string) {
		clearInterval(answeringInterval); // stop the timer
		console.log(answer, 'submitted by', name);
		answeringInterval = undefined;

		// // prevent submitting for an old question
		if (activeQuestionKey !== selectedQuestion.question) {
			console.log('hmm?');
			console.log('Ignored stale submit for:', activeQuestionKey);
			return;
		}
		socket.emit('checkAnswer', {
			answer: answer.trim(),
			question: selectedQuestion,
			socketId: socket.id
		});
	}

	$effect(() => {
		// side effects, use this rune sparingly! like playing sounds.
		if (isCorrect) {
			new Audio('https://www.myinstants.com/media/sounds/rightanswer.mp3').play();
		}
	});
	
</script>

<div class="modal" transition:scale={{ delay: 200 }}>
	<div class="modal-content">
		<div class="question">
			{selectedQuestion.question}
			{#if selectedQuestion.imgSrc}
				<img class="half-screen-img" src={selectedQuestion.imgSrc} alt="question" />
			{/if}
		</div>

		{#if questionState() === QuestionState.Open}
			<!-- svelte-ignore a11y_no_static_element_interactions -->
			<Tooltip title={selectedQuestion.buzzers.includes(name) ? 'You have already buzzed in!' : ''}>
				<button
					disabled={selectedQuestion.buzzers.includes(name)}
					class="buzz-button"
					onclick={() => setBuzzed(true)}
				>
					BUZZ IN!
				</button>
			</Tooltip>
		{:else if questionState() === QuestionState.Guessing}
			{#if whoBuzzed !== name}
				<p>{whoBuzzed} buzzed in first! guessing...</p>
			{:else}
				{#if isAnswering}
					<p>You have {secondsLeft} seconds to answer!</p>
				{/if}
				<input bind:value={userAnswer} placeholder="Enter your answer" />
				<button
					onclick={() => {
						submitAnswer(userAnswer);
					}}>Submit</button
				>
			{/if}
		{:else if questionState() === QuestionState.ShowAnswer}
			<div class="answer">
				Correct Answer: {selectedQuestion.answer}
			</div>
		{/if}
		{#if guess !== '' && !isCorrect}
			<p>{guess} is Incorrect!</p>
		{/if}

		<button
			class="back-btn"
			disabled={questionState() !== QuestionState.ShowAnswer}
			onclick={backToBoard}
		>
			Back to Board
		</button>
	</div>
</div>

<style>
	.question {
		font-size: 1.5rem;
		margin-bottom: 1rem;
	}
	.modal {
		position: fixed;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background: rgba(0, 0, 0, 0.9);
		display: grid;
		place-items: center;
		opacity: 0.9;
	}

	.modal-content {
		background: var(--theme-color);
		color: white;
		padding: 2rem;
		border-radius: 10px;
		max-width: 800px;
		text-align: center;
		overflow: scroll;
	}

	:global(.half-screen-img) {
		max-width: 100%;
		max-height: 70vh;
		height: auto;
		display: block;
		object-fit: contain; /* Ensures the whole image is visible */
	}

	.buzz-button {
		background: var(--point-color);
		color: #000;
		padding: 1rem 2rem;
		border: none;
		border-radius: 5px;
		cursor: pointer;
		font-size: 1.5rem;
		margin: 1rem;
	}
	.buzz-button:disabled {
		opacity: 0.5;
		cursor: not-allowed;
	}

	.back-btn:disabled {
		opacity: 0.5;
		cursor: not-allowed;
	}
</style>
