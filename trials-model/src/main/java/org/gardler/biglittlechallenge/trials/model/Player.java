package org.gardler.biglittlechallenge.trials.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Player represents a player in a game, this may be an AI or a human player.
 *
 */
public class Player extends org.gardler.biglittlechallenge.core.model.Player {
	private static final long serialVersionUID = -4194853817643572541L;
	private static Logger logger = LoggerFactory.getLogger(Player.class);
	
	public Player(String name, String uiClassName) {
		super(name, uiClassName);		
		logger.debug("Created a player with the UI class " + uiClassName);
	}

}
