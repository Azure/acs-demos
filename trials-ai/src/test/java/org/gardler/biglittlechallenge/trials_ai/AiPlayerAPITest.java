package org.gardler.biglittlechallenge.trials_ai;

import java.util.ArrayList;

import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Round;
import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.api.AbstractPlayerAPI;
import org.gardler.biglittlechallenge.trials.model.Player;
import org.gardler.biglittlechallenge.trials.model.PlayerStatus;

import junit.framework.TestCase;

public class AiPlayerAPITest extends TestCase {
	public void testGetGameStatus() {
		AbstractPlayerAPI api = new AiPlayerAPI("Test AI Player", new DumbAIUI());
		PlayerStatus result = api.getGameStatus();
		assertEquals("Game status is not 'Idle'", PlayerStatus.State.Idle, result.getStatus());
	}
	
	public void testPutGameStatus() {
		AbstractPlayerAPI api = new AiPlayerAPI("Test AI Player", new DumbAIUI());
		PlayerStatus status = new PlayerStatus();
		status.setStatus(PlayerStatus.State.Ready);
		PlayerStatus result = api.putGameStatus(status);
		assertEquals("Game status is not 'ready'", PlayerStatus.State.Ready, result.getStatus());
	}
	
	public void testGetCardsForEvent() {
		AbstractPlayerAPI api = new AiPlayerAPI("Test AI Player", new DumbAIUI());
		PlayedCards cards = api.getCardsForRound(new Round("Test Round"));
		assertEquals("We should have only one card for an event", 1, cards.size());
	}
	
	public void testPostResult() {
		DumbAIUI ui = new DumbAIUI();
		Player winner = new Player("Winning player", ui);
		Player runnerup = new Player("Runner up player", ui);
		Player player = new Player("Test AI Player", ui);
		
		Round round = new Round("Test Round");
		round.setGameID("gameUID");
		ArrayList<org.gardler.biglittlechallenge.core.model.Player> playerPositions = new ArrayList<org.gardler.biglittlechallenge.core.model.Player>();
		playerPositions.add(winner);
		playerPositions.add(runnerup);
		playerPositions.add(player);
		round.setPlayerPositions(playerPositions);
		
		AbstractPlayerAPI api = new AiPlayerAPI(player, ui);
		PlayerStatus response = api.postResult(round);
		assertEquals("Game status is not 'Playing'", PlayerStatus.State.Playing, response.getStatus());
	}

}
