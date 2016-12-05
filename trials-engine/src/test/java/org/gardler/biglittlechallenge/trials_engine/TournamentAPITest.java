package org.gardler.biglittlechallenge.trials_engine;

import static org.junit.Assert.*;

import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.engine.TournamentAPI;
import org.gardler.biglittlechallenge.trials.model.GameStatus;
import org.gardler.biglittlechallenge.trials.model.Player;
import org.junit.Test;

public class TournamentAPITest {

	@Test
	public void testPostJoinGame() {
		DumbAIUI ui = new DumbAIUI();
		Player player = new Player("Test AI Player", ui); 
		
		TournamentAPI api = new TournamentAPI();
		GameStatus status = api.postJoinGame(player);
		
		assertEquals("Game status is not 'Waiting'", GameStatus.State.Waiting, status.getStatus());
	}

	@Test
	public void testGetStatus() {
		TournamentAPI api = new TournamentAPI();
		GameStatus status = api.getStatus();
		assertEquals("Game status is not 'Idle'", GameStatus.State.Idle, status.getStatus());
	}

}
