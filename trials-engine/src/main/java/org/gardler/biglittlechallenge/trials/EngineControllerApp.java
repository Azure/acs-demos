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
 * The EngineControllerApp is responsible fr starting and running Tournament API instances.
 *
 */
public class EngineControllerApp 
{
	
	private static Logger logger = LoggerFactory.getLogger(EngineControllerApp.class);
	
	// Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/v0.1/";

        
    public static void main( String[] args )
    {
        logger.debug("Starting the trials card game.");
        
        String numString = System.getenv("MIN_NUMBER_OF_PLAYERS");
        if (numString == null) {
        	numString = "2";
        }
        int players = Integer.parseInt(numString);
        
        // FIXME: use players to set the number of players needed before the game starts
    	logger.debug("Create a tournament server");
    	startServer();
    	
    	while (true) {
    		
    	}
    }

	public static HttpServer startServer() {
		TournamentAPI api = new TournamentAPI(new Tournament());
		final ResourceConfig rc = new ResourceConfig().register(api);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

}
