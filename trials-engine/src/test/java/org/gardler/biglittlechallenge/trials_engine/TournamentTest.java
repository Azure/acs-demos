package org.gardler.biglittlechallenge.trials_engine;

import org.gardler.biglittlechallenge.trials.engine.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class TournamentTest extends TestCase {
	
	private Tournament tournament;
	private static Logger logger = LoggerFactory.getLogger(TournamentTest.class);

	public void testToString() {
		this.tournament = new Tournament();
		String result = this.tournament.toString();
		logger.info("Tournament toString is: " + result);
		assertTrue("We don;t appear to have set the rounds in the tournament correctly", result.contains("consists of 4 events"));
	}

}
