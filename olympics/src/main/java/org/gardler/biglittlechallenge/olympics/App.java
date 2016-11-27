package org.gardler.biglittlechallenge.olympics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;
import org.gardler.biglittlechallenge.olympics.tournament.Tournament;
import org.gardler.biglittlechallenge.olympics.ui.Shell;
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

        logger.debug("Create Shell UI");
    	Shell shell = new Shell();
        
    	logger.debug("Create players (5 AI, two human)");
    	List<Player> players = new ArrayList<Player>();
    	players.add(new DumbPlayer("AI Player One"));
    	players.add(new DumbPlayer("AI Player Two"));
    	players.add(new DumbPlayer("AI Player Three"));
    	players.add(new DumbPlayer("AI Player Four"));
    	players.add(new DumbPlayer("AI Player Five"));
    	players.add(new org.gardler.biglittlechallenge.olympics.model.Player("Ross", shell));
    	players.add(new org.gardler.biglittlechallenge.olympics.model.Player("Zeph", shell));
    	shell.setPlayers(players);
    	
    	logger.debug("Create tournament");
    	Tournament tournament = new Tournament(players);
    	shell.setTournament(tournament);
    	
    	try {
    		logger.debug("Start the game using the Shell UI");
			shell.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}
