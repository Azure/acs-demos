package org.gardler.biglittlechallenge.trials_engine;

import static org.junit.Assert.*;

import org.gardler.biglittlechallenge.core.api.model.GameStatusResponse;
import org.gardler.biglittlechallenge.core.model.GameStatus;
import org.gardler.biglittlechallenge.trials.engine.Tournament;
import org.gardler.biglittlechallenge.trials.engine.TournamentAPI;
import org.junit.Test;

public class TournamentAPITest {

	@Test
	public void testGetStatus() {
		TournamentAPI api = initTournament();
		GameStatusResponse status = api.getStatus();
		assertEquals("Game status is not 'Idle'", GameStatus.State.Idle, status.getState());
	}

	private TournamentAPI initTournament() {
		Tournament tournament = new Tournament();
		TournamentAPI api = new TournamentAPI(tournament);
		api.abortGame(tournament.getID()); // This is to ensure we are not using a previously created game
		return api;
	}

}
