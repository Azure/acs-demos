package org.gardler.biglittlechallenge.trials_engine;

import static org.junit.Assert.*;

import org.gardler.biglittlechallenge.core.model.GameStatus;
import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.engine.TournamentAPI;
import org.gardler.biglittlechallenge.trials.model.Player;
import org.junit.Test;

public class TournamentAPITest {

	@Test
	public void testPostJoinGame() {
		DumbAIUI ui = new DumbAIUI();
		Player player = new Player("Test AI Player", ui); 
		
		TournamentAPI api = new TournamentAPI();
		GameStatus status = api.postJoinGame(player);
		
		assertEquals("Game status is not 'WaitingForPlayers'", GameStatus.State.WaitingForPlayers, status.getState());
		
		player = new Player("Second Test AI Player", ui);
		status = api.postJoinGame(player);
		
		assertEquals("Game status is not 'Playing' after second player joins", GameStatus.State.Playing, status.getState());		
	}

	@Test
	public void testGetStatus() {
		TournamentAPI api = initTorunament();
		GameStatus status = api.getStatus();
		assertEquals("Game status is not 'WaitingForPlayers'", GameStatus.State.WaitingForPlayers, status.getState());
	}

	private TournamentAPI initTorunament() {
		TournamentAPI api = new TournamentAPI();
		api.abortGame();
		return api;
	}

}
