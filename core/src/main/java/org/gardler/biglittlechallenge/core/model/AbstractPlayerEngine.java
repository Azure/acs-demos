package org.gardler.biglittlechallenge.core.model;

import java.net.InetAddress;
import java.net.URI;

import org.gardler.biglittlechallenge.core.model.PlayerStatus.State;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPlayerEngine implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(AbstractPlayerEngine.class);
	
	// Base URI the Grizzly HTTP server will listen on
	protected static final String BASE_HOST = "http://0.0.0.0";
	protected static final String BASE_PATH = "/api/v0.1/";
	
	protected int port;

	protected static int cooloff = 0; // time to wait before requesting to join a
									// new game
	
	public AbstractPlayerEngine(int port) {
		this.port = port;
	}
	
	protected void startGameLoop(Player player, String engineEndpoint) {
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
				// logger.info("Requesting a game we can join");
				break;
			case Waiting:
				// logger.info("Waiting for game to indicate ready to start");
				break;
			case Ready:
				// logger.info("Game is ready, we are ready, almost there");
				break;
			case Playing:
				// logger.debug("Playing");
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

	public HttpServer startAPIServer(Player player, int port) {
		logger.debug("Starting Player API server at " + getURI(port));

		player.setEndpoint(getURI(port));
		
		PlayerAPI api = new PlayerAPI(player);
		final ResourceConfig rc = new ResourceConfig().register(api);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:" + port + BASE_PATH),
				rc);
		return server;
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
