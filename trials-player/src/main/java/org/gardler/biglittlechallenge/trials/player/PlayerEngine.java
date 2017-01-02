package org.gardler.biglittlechallenge.trials.player;

import java.net.InetAddress;
import java.util.UUID;

import org.gardler.biglittlechallenge.core.model.AbstractPlayerEngine;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.trials.ai.player.AiPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A runnable class used to start the AI Player API and AI Player logic engine.
 *
 */
public class PlayerEngine extends AbstractPlayerEngine {
	private static Logger logger = LoggerFactory.getLogger(PlayerEngine.class);

	/**
	 * Create a player engine on the supplied port.
	 * 
	 * @param port
	 */
	public PlayerEngine(int port) {
		super(port);
	}

	public void run() {
		logger.debug("Starting Player API server at " + getURI(port));

		String engineEndpoint = AiPlayer.getEngineEndoint();
		
		UUID id = UUID.randomUUID();
		Player player = new Player("Player " + id, "org.gardler.biglittlechallenge.trials.player.ui.Shell");
		startAPIServer(player, port);
		
		startGameLoop(player, engineEndpoint);
	}

	private String getURI(int port) {
		String uri;
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			uri = "http://" + ip + ":" + port + BASE_PATH;
			return uri;
		} catch (Exception e) {
			logger.warn("Unable to find hostname, trying localhost", e);
			return "localhost";
		}
	}

}
