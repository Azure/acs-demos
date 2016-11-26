package org.gardler.biglittlechallenge.olympics;

import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.Sprint100m;
import org.gardler.biglittlechallenge.olympics.tournament.Track8000m;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
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
    	
    	Sprint100m sprint = new Sprint100m(players);
    	sprint.playHand();
    	
    	Track8000m track8000 = new Track8000m(players);
    	track8000.playHand();
    }


}
