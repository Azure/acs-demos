package org.gardler.biglittlechallenge.trials_player;

import org.gardler.biglittlechallenge.trials_player.model.PlayerStatus;

import junit.framework.TestCase;

public class APITest extends TestCase {

	public void testGetGameStatus() {
		API api = new API();
		PlayerStatus result = api.getGameStatus();
		assertEquals("Game status is not 'waiting'", PlayerStatus.State.Waiting, result.getStatus());
	}
	
	public void testPutGameStatus() {
		API api = new API();
		PlayerStatus status = new PlayerStatus();
		status.setStatus(PlayerStatus.State.Ready);
		PlayerStatus result = api.putGameStatus(status);
		assertEquals("Game status is not 'ready'", PlayerStatus.State.Ready, result.getStatus());
	}

}
