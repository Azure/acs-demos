package org.gardler.biglittlechallenge.trials_player;

import org.gardler.biglittlechallenge.trials_player.model.PlayerStatus;

import junit.framework.TestCase;

public class APITest extends TestCase {

	public void testPutGameStatus() {
		API api = new API();
		PlayerStatus result = api.putGameStatus();
		assertTrue("Game status response does not include gameUID", result.getGameUID() != null);
	}

}
