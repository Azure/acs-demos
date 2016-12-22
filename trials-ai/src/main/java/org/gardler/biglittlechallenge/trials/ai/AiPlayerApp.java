package org.gardler.biglittlechallenge.trials.ai;

import java.net.URI;

import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.PlayerAPI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main application that should be run to create an AI player in
 * Trials.
 *
 */
public class AiPlayerApp {
	private static Logger logger = LoggerFactory.getLogger(AiPlayerApp.class);
	
	protected static Player player;
	// Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8888/api/v0.1/";
    
    public static void main( String[] args )
    {	AiPlayerApp.startServer();
        logger.info(String.format("Client API started with WADL available at "
                + "%sapplication.wadl", BASE_URI));
        
        // Request to join a game
    	String engineEndpoint;
		engineEndpoint = AiPlayer.getEngineEndoint();
    	AiPlayer player = new AiPlayer("AI Player 1");
        player.joinTournament(engineEndpoint);
        
        while (true) {
        	// Interactions are now via the player API
        }
    }
    
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().register(PlayerAPI.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

}
