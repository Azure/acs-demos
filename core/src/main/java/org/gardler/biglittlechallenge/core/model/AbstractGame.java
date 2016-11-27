package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractGame represents a single game. It may consist of one or more rounds.
 *
 */
public abstract class AbstractGame {

	protected AbstractHands hands;
	protected List<Player> players;
	
	public AbstractGame(List<Player> players) {
		this.players = players;
		this.setHands();
	}
	
	/**
	 * Set the hands to be played in this game.
	 */
	protected abstract void setHands();
	
	/**
	 * Get an ordered List of the hands to be played in this game.
	 */
	public ArrayList<Hand> getHands() {
		return hands.getAsList();
	}

}
