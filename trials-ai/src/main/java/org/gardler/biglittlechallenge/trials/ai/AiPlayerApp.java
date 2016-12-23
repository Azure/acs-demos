package org.gardler.biglittlechallenge.trials.ai;

import java.net.URI;
import java.util.UUID;

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
    public static final String BASE_HOST = "http://0.0.0.0"; 
	public static final String BASE_PATH = "/api/v0.1/";
    
    public static void main( String[] args ) {	
    	int port;
    	if (args.length == 0) {
    		// TODO: There is still a chance of a port clash, should verify port is available
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
    	AiPlayerApp.startServer(player);
        
    	player.joinTournament(engineEndpoint);
        
        while (true) {
        	// Interactions are now via the player API
        }
    }
    
    public static HttpServer startServer(AiPlayer player) {
    	PlayerAPI api = new PlayerAPI(player);
        final ResourceConfig rc = new ResourceConfig().register(api);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(player.getEndpoint()), rc);
        return server;
    }
    
    private static String getURI(int port) {
    	String uri = BASE_HOST + ":" + port + BASE_PATH; 
    	return uri;
    }

}
