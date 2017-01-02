package org.gardler.biglittlechallenge.test;

import org.gardler.biglittlechallenge.trials.Engine;
import org.gardler.biglittlechallenge.trials.ai.PlayerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test application will start a game engine for a specified number of
 * players. Use the MIN_NUMBER_OF_PLAYERS environment variable to set the number
 * of players.
 *
 */
public class TestApp {
	private static Logger logger = LoggerFactory.getLogger(TestApp.class);

	public static final String ENGINE_BASE_URI = "http://0.0.0.0:8080/api/v0.1/";

	public static void main(String[] args) {
		String numString = System.getenv("MIN_NUMBER_OF_PLAYERS");
		if (numString == null) {
			numString = "2";
		}
		int players = Integer.parseInt(numString);
		
		logger.debug("Starting the trials card game for " + players + " players.");

		Engine gameEngine = new Engine(players);
		new Thread(gameEngine).start();

		for (int i = 1; i <= players; i++) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			int port = (int) (8000 + Math.round((Math.random() * 9999)));
			PlayerEngine playerEngine = new PlayerEngine(port);
			new Thread(playerEngine).start();
		}

		while (true) {
			// Just keep the app running
		}
	}
}
