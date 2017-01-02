package org.gardler.biglittlechallenge.trials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple wrapper application that will start an instance of the game Engine.
 * 
 * Configuration is via Environment variables:
 * 
 * MIN_NUMBER_OF_PLAYERS is the minimum number of players the game should have
 *
 */
public class EngineControllerApp 
{	
	private static Logger logger = LoggerFactory.getLogger(EngineControllerApp.class);
	    
    public static void main( String[] args )
    {
        logger.debug("Starting the trials card game.");
        String numString = System.getenv("MIN_NUMBER_OF_PLAYERS");
        if (numString == null) {
        	numString = "2";
        }
        int players = Integer.parseInt(numString);        

        Engine engine = new Engine(players);
        engine.run();
    }
}
