package org.gardler.biglittlechallenge.trials_engine;

import static org.junit.Assert.*;

import org.gardler.biglittlechallenge.core.model.GameStatus;
import org.gardler.biglittlechallenge.trials.engine.TournamentAPI;
import org.gardler.biglittlechallenge.trials.model.Player;
import org.junit.Test;

public class TournamentAPITest {

	@Test
	public void testFullGame() {
		String uiClassName = "org.gardler.biglittlechallenge.trials.ai.DumbAIUI";
		Player player = new Player("Test AI Player", uiClassName); 
		
		TournamentAPI api = initTournament();
		GameStatus status = api.postJoinGame(player);
		assertEquals("Game status is not 'WaitingForPlayers'", GameStatus.State.WaitingForPlayers, status.getState());
		
		player = new Player("Second Test AI Player", uiClassName);
		status = api.postJoinGame(player);
		assertEquals("Game status is not 'Playing' after second player joins", GameStatus.State.Playing, status.getState());
		
		// Verify engine waits for players to call back
	}

	@Test
	public void testGetStatus() {
		TournamentAPI api = initTournament();
		GameStatus status = api.getStatus();
		assertEquals("Game status is not 'WaitingForPlayers'", GameStatus.State.WaitingForPlayers, status.getState());
	}

	private TournamentAPI initTournament() {
		TournamentAPI api = new TournamentAPI();
		api.abortGame();
		return api;
	}

}
