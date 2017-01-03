package org.gardler.biglittlechallenge.trials.player;

import java.io.IOException;
import java.net.InetAddress;

import org.gardler.biglittlechallenge.core.AbstractPlayerEngine;
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
	private Player player;

	/**
	 * Create a player engine on the supplied port.
	 * 
	 * @param port
	 */
	public PlayerEngine(int port, String playerName) {
		super(port);
		try {
			player = Player.load(playerName);
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load the player", e);
		} catch (IOException e) {
			// save file does not exist so create a new player
			player = new Player(playerName, "org.gardler.biglittlechallenge.trials.player.ui.Shell");
		}		
	}

	public void run() {
		logger.debug("Starting Player API server at " + getURI(port));

		String engineEndpoint = AiPlayer.getEngineEndoint();
		
		startAPIServer(this.player, port);
		
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
