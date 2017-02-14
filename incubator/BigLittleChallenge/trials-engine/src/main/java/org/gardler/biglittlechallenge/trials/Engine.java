package org.gardler.biglittlechallenge.trials;

import java.net.URI;

import org.gardler.biglittlechallenge.trials.engine.Tournament;
import org.gardler.biglittlechallenge.trials.engine.TournamentAPI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main Engine class. This is a Runnable task that should be run in a
 * separate thread to the main controlling application. It will create the
 * engine instance and start the API server.
 * 
 */
public class Engine implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(Engine.class);

	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://0.0.0.0:8080/api/v0.1/";

	private int numberOfPlayers;

	/**
	 * Create an instance of the engine that will manage a game for the
	 * indicated number of players.
	 * 
	 * @param numOfPlayers
	 *            - number of players required for the game to start
	 */
	public Engine(int numOfPlayers) {
		this.numberOfPlayers = numOfPlayers;
	}

	public void run() {
		logger.debug("Starting the trials engine.");

		logger.debug("Create a tournament server for " + numberOfPlayers + " players");
		startServer(numberOfPlayers);

		while (true) {

		}
	}

	/**
	 * Start the API server.
	 * 
	 * @param players
	 * @return
	 */
	public static HttpServer startServer(int players) {
		TournamentAPI api = new TournamentAPI(new Tournament(players));
		final ResourceConfig rc = new ResourceConfig().register(api);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

}
