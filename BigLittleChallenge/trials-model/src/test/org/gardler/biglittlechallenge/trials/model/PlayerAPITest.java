package org.gardler.biglittlechallenge.trials.model;

import org.gardler.biglittlechallenge.core.api.PlayerAPI;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.PlayerStatus;
import org.gardler.biglittlechallenge.core.model.Round;
import org.junit.Before;

import junit.framework.TestCase;

public class PlayerAPITest extends TestCase {
	
	PlayerAPI api;
	
	@Before
	public void setUp() {
		String uiClassName = "org.gardler.biglittlechallenge.trials.model.TestPlayerUI";
		Player player = new Player("Test AI Player", uiClassName);
		api = new PlayerAPI(player);
		PlayerStatus status = player.getStatus();
		
		status.setState(PlayerStatus.State.Idle);
		
	}
	
	public void testGetStatus() {
		PlayerStatus result = api.getStatus();
		assertEquals("Game status is not 'Idle'", PlayerStatus.State.Idle, result.getState());
	}
	
	public void testPutStatus() {
		PlayerStatus status = new PlayerStatus();
		status.setState(PlayerStatus.State.Ready);
		PlayerStatus result = api.putGameStatus(status);
		assertEquals("Game status is not 'ready'", PlayerStatus.State.Ready, result.getState());
		
		status.setState(PlayerStatus.State.Idle);
		result = api.putGameStatus(status);
		assertEquals("Game status is not 'Idle'", PlayerStatus.State.Idle, result.getState());
	}
	
	public void testGetCardsForEvent() {
		PlayedCards cards = api.getCardsForRound(new Round("Test Round"));
		assertEquals("We should have only one card for an event", 1, cards.size());
	}
	
	public void testPostResult() {
		String uiClassName = "org.gardler.biglittlechallenge.trials.ai.DumbAIUI";
		Player player = new Player("Test AI Player", uiClassName);
		
		Round round = new Round("Test Round");
		round.setGameID("gameUID");
		
		PlayerAPI api = new PlayerAPI(player);
		PlayerStatus response = api.postResult(round);
		assertEquals("Game status is not 'Playing'", PlayerStatus.State.Idle, response.getState());
	}

}
