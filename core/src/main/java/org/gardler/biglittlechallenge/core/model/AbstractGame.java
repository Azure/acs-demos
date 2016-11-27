package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractGame represents a single game. It may consist of one or more rounds.
 *
 */
public abstract class AbstractGame {

	protected List<Hand> hands = new ArrayList<Hand>();
	protected List<Player> players;
	
	public AbstractGame(List<Player> players) {
		this.players = players;
		this.setHands();
	}
	
	/**
	 * Set the rounds to be played in this game.
	 */
	protected abstract void setHands();
	
	public List<Hand> getHands() {
		return hands;
	}

}
