package org.gardler.biglittlechallenge.trials.ai;

import org.gardler.biglittlechallenge.core.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create an AI player that will try to connect to a Game Engine and
 * play a game every 30 seconds.
 * 
 * The AI is very simple so don't expect a challenging game, but it's good for
 * testing.
 *
 */
public class AiPlayerApp {
	private static Logger logger = LoggerFactory.getLogger(AiPlayerApp.class);

	protected static Player player;
	
	
	public static void main(String[] args) {
		logger.info("Starting a Player Engine.");
		int port;
		if (args.length == 0) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			port = (int) (8000 + Math.round((Math.random() * 9999)));
		} else {
			port = Integer.parseInt(args[0]);
		}
		
		PlayerEngine engine = new PlayerEngine(port);
		engine.run();
		
		while (true) {
			
		}
	}


}
