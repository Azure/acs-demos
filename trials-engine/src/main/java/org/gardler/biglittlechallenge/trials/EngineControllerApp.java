package org.gardler.biglittlechallenge.trials;

import org.gardler.biglittlechallenge.trials.engine.Tournament;
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
        
    	logger.debug("Create a tournament server");
    	Tournament tournament = new Tournament();
    	tournament.startServer();
    }

}
