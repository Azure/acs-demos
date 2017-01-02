package org.gardler.biglittlechallenge.trials.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a single human player with a shell UI and connect to a game.
 *
 */
public class PlayerApp {
	private static Logger logger = LoggerFactory.getLogger(PlayerApp.class);
	
	public static void main(String[] args) {
		logger.info("Starting a Human Player Engine.");
		int port;
		if (args.length == 0) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			port = (int) (8000 + Math.round((Math.random() * 9999)));
		} else {
			port = Integer.parseInt(args[0]);
		}
		
		PlayerEngine engine = new PlayerEngine(port, "test_1");
		engine.run();
		
		while (true) {
			
		}
	}
}
