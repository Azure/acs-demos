package org.gardler.biglittlechallenge.test;

import org.gardler.biglittlechallenge.trials.Engine;
import org.gardler.biglittlechallenge.trials.ai.AiPlayerEngine;
import org.gardler.biglittlechallenge.trials.player.PlayerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test application will start a game engine for a specified number of
 * players. Use the MIN_NUMBER_OF_PLAYERS environment variable to set the number
 * of players (defaults to 2). Use the NUMBER_OF_HUMAN_PLAYERS to set the number
 * of players that will require human interaction (using the Shell UI by
 * default, defaults to 0 human players).
 *
 */
public class TestApp {
	private static Logger logger = LoggerFactory.getLogger(TestApp.class);

	public static final String ENGINE_BASE_URI = "http://0.0.0.0:8080/api/v0.1/";

	public static void main(String[] args) {
		String numString = System.getenv("MIN_NUMBER_OF_PLAYERS");
		if (numString == null) {
			numString = "1"; // default is actually 2 because when AI Player is created we ask for 2 player game
		}
		int players = Integer.parseInt(numString);

		numString = System.getenv("NUMBER_OF_HUMAN_PLAYERS");
		if (numString == null) {
			numString = "0";
		}
		int numOfHumanPlayers = Integer.parseInt(numString);
		int numOfAiPlayers = players - numOfHumanPlayers;

		logger.debug("Starting the trials card game for " + players + " players.");
		Engine gameEngine = new Engine(players);
		new Thread(gameEngine).start();

		logger.debug("Creating " + numOfAiPlayers + " AI players");
		for (int i = 1; i <= numOfAiPlayers; i++) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			int port = (int) (8000 + Math.round((Math.random() * 9999)));
			AiPlayerEngine playerEngine = new AiPlayerEngine(port);
			new Thread(playerEngine).start();
		}

		logger.debug("Creating " + numOfHumanPlayers + " human plyaers");
		for (int i = 1; i <= numOfHumanPlayers; i++) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			int port = (int) (8000 + Math.round((Math.random() * 9999)));
			PlayerEngine playerEngine = new PlayerEngine(port, "test_" + i);
			new Thread(playerEngine).start();
		}

		while (true) {
			// Just keep the app running
		}
	}
}
