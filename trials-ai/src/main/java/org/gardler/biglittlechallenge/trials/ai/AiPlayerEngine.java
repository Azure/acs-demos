package org.gardler.biglittlechallenge.trials.ai;

import java.util.UUID;

import org.gardler.biglittlechallenge.core.model.AbstractPlayerEngine;
import org.gardler.biglittlechallenge.trials.ai.player.AiPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A runnable class used to start the AI Player API and AI Player logic engine.
 *
 */
public class AiPlayerEngine extends AbstractPlayerEngine {
	private static Logger logger = LoggerFactory.getLogger(AiPlayerEngine.class);

	/**
	 * Create a player engine on the supplied port.
	 * 
	 * @param port
	 */
	public AiPlayerEngine(int port) {
		super(port);
	}

	public void run() {
		String engineEndpoint = AiPlayer.getEngineEndoint();
		
		UUID id = UUID.randomUUID();
		AiPlayer player = new AiPlayer("AI Player " + id);
		startAPIServer(player, port);
		
		startGameLoop(player, engineEndpoint);
	}

}
