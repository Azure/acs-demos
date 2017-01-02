package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;

/**
 * Summarizes a single players results in a single game.
 * For detailed results in each round see RoundResults.
 *
 */
public class PlayerResults implements Serializable {
	private static final long serialVersionUID = 2385515664418996288L;
	Player player;
	int points;
	
	public PlayerResults() {
		super();
	}

	public PlayerResults(Player player, int points) {
		super();
		this.player = player;
		this.points = points;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Get the total points scored by this player.
	 */
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
