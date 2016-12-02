package org.gardler.biglittlechallenge.trials.model;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Player represents a player in a game, this may be an AI or a human player.
 *
 */
public class Player extends org.gardler.biglittlechallenge.core.model.Player {

	private static Logger logger = LoggerFactory.getLogger(Player.class);
	
	public Player(String name, AbstractUI ui) {
		super(name, ui);		
		logger.debug("Created a player using the " + ui.getClass() + " UI");
	}

}
