<script lang="ts">
	import type { PlayerData, Question } from '$lib/index';
	import io from 'socket.io-client';
	import QuestionCard from '../components/QuestionCard.svelte';
	import PlayersWidget from '../components/PlayersWidget.svelte';

	let questionData = $state([] as { title: string; questions: Question[] }[]);
	let players = $state([] as PlayerData[]);
	let name = $state('');
	let isNameModal = $state(true);
	let submitName = $state(false);
	let whoControls = $state('');
	let joinError = $state(false);
	let isGameOver = $state(false);
	let winnerNames = $derived.by(() => {
		if (players.length === 0) return '';
		const maxScore = Math.max(...players.map((p) => p.score));
		const winners = players.filter((p) => p.score === maxScore).map((p) => p.name);
		return winners.join(', ');
	});

	const socket = io();

	// SOCKET LISTEN EVENTS
	socket.on('errorJoin', (message: string) => {
		if (message === 'duplicateName') {
			submitName = false;
			joinError = true;
			console.log('Name already taken');
		} else {
		}
	});
	socket.on('playerJoined', () => {
		isNameModal = false;
		joinError = false;
		submitName = false;
	});
	socket.on('questionData', (data: { title: string; questions: Question[] }[]) => {
		questionData = data;
		console.log('Question data updated:', data);
	});
	socket.on('playerData', (data: PlayerData[]) => {
		players = data;
		console.log('Updated player data updated:', data);
	});
	socket.on('whoControls', (socketId: string) => {
		whoControls = socketId;
		console.log('Current controller:', socketId);
	});
	socket.on('selectQuestion', (question: Question) => {
		handleSelect(question);
	});
	socket.on('gameOver', () => {
		// show modal for the winner and reset the game
		isGameOver = true;
	});
	socket.on('resetGame', () => {
		isGameOver = false;
		selectedQuestion = null;
	});

	// make selectedQuestion a type of optional question
	let selectedQuestion: Question | null | undefined = $state(null);

	// When someone selects a question.
	function handleSelectAndEmit(question: Question) {
		// Prevent selecting an already answered question or if the user is not in control
		console.log(whoControls, socket.id);
		if (question.answered || whoControls !== socket.id) return;
		handleSelect(question);
		socket.emit('selectQuestion', question);
	}

	// Followers handle select locally
	function handleSelect(question: Question) {
		selectedQuestion = question;
	}
	function backToBoard() {
		selectedQuestion = null;
		socket.emit('selectQuestion', null);
	}
</script>

<PlayersWidget {players} />
{#if isNameModal}
	<div class="name-entry">
		<h2>
			{#if joinError}Name already taken. Please choose a different name:
			{:else}Enter your name to join the game:
			{/if}
		</h2>

		<input bind:value={name} placeholder="Your name" />
		<button
			onclick={() => {
				if (name.trim()) {
					socket.emit('join', { name: name.trim(), socketId: socket.id, score: 0 });
					submitName = true;
				}
			}}
		>
			{#if submitName}
				<img src="/loading.svg" alt="loading" width="32" height="32" />
			{:else}
				Join Game
			{/if}
		</button>
	</div>
{/if}

{#if isGameOver}
	<div class="game-over">
		<h2>Game Over</h2>
		<p>The winner is: {winnerNames}</p>
		{#if whoControls === socket.id}<button
				onclick={() => {
					socket.emit('resetGame');
				}}>Play Again</button
			>
		{/if}
	</div>
{/if}

<div class="board {isNameModal || isGameOver ? 'blurred' : ''}">
	{#each questionData as category}
		<div>
			<h2 class="category">{category.title.toUpperCase()}</h2>
			{#each category.questions as question}
				<!-- svelte-ignore a11y_click_events_have_key_events -->
				<!-- svelte-ignore a11y_no_static_element_interactions -->
				<div
					class="question-card {question.answered ? 'answered' : ''} {selectedQuestion === question
						? 'selected'
						: 'unselected'}"
					onclick={() => handleSelectAndEmit(question)}
				>
					{#if !question.answered}
						${question.points}
					{/if}
				</div>
			{/each}
		</div>
	{/each}
</div>

{#if selectedQuestion}
		<QuestionCard {selectedQuestion} {name} {socket} {backToBoard} />
{/if}

<style>
	@import url('https://fonts.cdnfonts.com/css/itc-korinna-std');

	:root {
		--theme-color: #060ce9;
		--point-color: goldenrod;
		font-family: 'ITC_ Korinna', sans-serif;
		background-color: black;
	}

	:global(input) {
		padding: 0.5rem;
		font-size: 1.2rem;
		margin: 1rem;
		background-color: transparent;
		border: 3px solid var(--point-color);
		border-radius: 5px;
		color: white;
	}

	:global(input:active, input:focus-visible) {
		box-shadow: 2px 2px 15px var(--point-color) inset;
		outline: 0;
	}
	:global(button) {
		background: transparent;
		border: none;
		color: var(--point-color);
		cursor: pointer;
	}

	.blurred {
		pointer-events: none;
		user-select: none;
		filter: blur(4px);
		opacity: 0.6;
	}
	.name-entry,
	.game-over {
		text-align: center;
		margin: 2rem;
		color: white;
	}
	.board {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 1rem;
		padding: 2rem;
	}

	.category {
		background: var(--theme-color);
		color: white;
		padding: 1rem;
		text-align: center;
		justify-self: center;
		vertical-align: middle;
		min-height: 3rem;
	}

	.question-card {
		background-color: var(--theme-color);
		color: var(--point-color);
		padding: 1rem;
		font-size: 2rem;
		margin: 0.5rem 0;
		cursor: pointer;
		text-align: center;
		min-height: 3rem;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 1.5rem;
	}

	.question-card.answered {
		background: var(--theme-color);
		color: #888;
		cursor: default;
	}

	.question-card:hover:not(.answered) {
		transform: scale(1.05);
	}

	.selected {
		background-color: var(--point-color);
		color: black;
	}

	.unselected {
		background-color: var(--theme-color);
		color: var(--point-color);
	}
</style>
