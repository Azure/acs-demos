package org.gardler.biglittlechallenge.trials_ai;

import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.api.AbstractPlayerAPI;
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
		PlayedCards cards = api.getCardsForHand();
		assertEquals("We should have only one card for an event", 1, cards.size());
	}

}
