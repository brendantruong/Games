<script lang="ts">
	// This component displays the list of players currently in the game.
	let props = $props();
	let clicked = $state(false);
</script>

<div class="players-widget">
	<!-- modal: list players online, when clicked, it shows players names in a small dropdown -->

	<button onclick={() => (clicked = !clicked)}
		>{clicked ? '△ ' : '▽ '}{props.players.length} players online</button
	>
	{#if clicked}
		<ul>
			{#each props.players as player}
				<li>{player.name}: {player.score}</li>
			{/each}
		</ul>
	{:else}{/if}
</div>

<style>
	.players-widget button {
		background: transparent;
		border: none;
		color: var(--point-color);
		cursor: pointer;
	}
	.players-widget {
		color: white;
        opacity: 0.8;
		position: fixed;
		right: 1rem;
		background: var(--theme-color);
        border: white 2px solid;
		padding: 1rem;
		border-radius: 10px;
		max-width: 200px;
	}

	.players-widget ul {
		list-style-type: none;
		padding: 0;
	}

	.players-widget li {
		margin: 0.5rem 0;
	}
</style>
