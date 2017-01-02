package org.gardler.biglittlechallenge.trials.ai;

import java.net.InetAddress;
import java.net.URI;
import java.util.UUID;

import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.PlayerAPI;
import org.gardler.biglittlechallenge.core.model.PlayerStatus;
import org.gardler.biglittlechallenge.core.model.PlayerStatus.State;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
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
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_HOST = "http://0.0.0.0";
	public static final String BASE_PATH = "/api/v0.1/";
	private static int cooloff = 0; // time to wait before requesting to join a new game
	
	
	public static void main(String[] args) {
		int port;
		if (args.length == 0) {
			// TODO: There is still a chance of a port clash, should verify port
			// is available
			port = (int) (8000 + Math.round((Math.random() * 9999)));
		} else {
			port = Integer.parseInt(args[0]);
		}
		logger.debug("Starting Player API server at " + getURI(port));

		// Request to join a game
		String engineEndpoint = AiPlayer.getEngineEndoint();
		UUID id = UUID.randomUUID();
		AiPlayer player = new AiPlayer("AI Player " + id);
		player.setEndpoint(getURI(port));
		AiPlayerApp.startServer(player, port);

		while (true) {
			PlayerStatus status = player.getStatus();
			State state = status.getState();
			switch (state) {
			case Idle:
				try {
					logger.debug("Waiting for " + cooloff + "ms before requesting to join a new game.");
					Thread.sleep(cooloff);
				} catch (InterruptedException e) {
					logger.warn("Cooloff sleep time was interupted", e);
				}
				player.joinTournament(engineEndpoint);
				cooloff = 30000;
				break;
			case Requesting:
				//logger.info("Requesting a game we can join");
				break;
			case Waiting:
				//logger.info("Waiting for game to indicate ready to start");
				break;
			case Ready:
				//logger.info("Game is ready, we are ready, almost there");
				break;
			case Playing:
				//logger.debug("Playing");
				break;
			default:
				logger.warn("Player state is unrecognized: " + state);
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warn("Sleep got interupted", e);
			}
		}
	}

	public static HttpServer startServer(AiPlayer player, int port) {
		PlayerAPI api = new PlayerAPI(player);
		final ResourceConfig rc = new ResourceConfig().register(api);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:" + port + BASE_PATH),
				rc);
		return server;
	}

	private static String getURI(int port) {
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
