package org.gardler.biglittlechallenge.trials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.trials.ai.DumbPlayer;
import org.gardler.biglittlechallenge.trials.engine.Tournament;
import org.gardler.biglittlechallenge.trials_player.ui.Shell;
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
        logger.debug("Starting the trials card game.");

        logger.debug("Create Shell UI");
    	Shell shell = new Shell();
        
    	logger.debug("Create players (5 AI, two human)");
    	List<Player> players = new ArrayList<Player>();
    	players.add(new DumbPlayer("AI Player One"));
    	players.add(new DumbPlayer("AI Player Two"));
    	players.add(new DumbPlayer("AI Player Three"));
    	players.add(new org.gardler.biglittlechallenge.trials.model.Player("Human player 1", shell));
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
