package org.gardler.biglittlechallenge.olympics;

import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.Tournament;
import org.gardler.biglittlechallenge.olympics.tournament.event.Track100m;
import org.gardler.biglittlechallenge.olympics.tournament.event.Track8000m;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main application class. We run this to start a game.
 *
 */
public class App 
{
	
	private static Logger logger = LoggerFactory.getLogger(App.class);
        
    public static void main( String[] args )
    {
        logger.debug("Starting the Olympics card game.");
        
    	DumbPlayer player1 = new DumbPlayer("AI Player One");
    	DumbPlayer player2 = new DumbPlayer("AI Player Two");
    	DumbPlayer player3 = new DumbPlayer("AI Player Three");
    	DumbPlayer player4 = new DumbPlayer("AI Player Four");
    	DumbPlayer player5 = new DumbPlayer("AI Player Five");
    	Player[] players = { player1, player2, player3, player4, player5 };
    	
    	Tournament tournament = new Tournament(players);
    	tournament.start();
    }


}
