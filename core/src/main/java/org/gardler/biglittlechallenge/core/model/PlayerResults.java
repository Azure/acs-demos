package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;

import org.gardler.biglittlechallenge.core.api.model.GameTicket;

/**
 * Summarizes a single players results in a single game.
 * For detailed results in each round see RoundResults.
 *
 */
public class PlayerResults implements Serializable {
	private static final long serialVersionUID = 2385515664418996288L;
	GameTicket ticket;
	int points;
	
	public PlayerResults() {
		super();
	}

	public PlayerResults(GameTicket ticket, int points) {
		super();
		this.ticket = ticket;
		this.points = points;
	}
	
	public GameTicket getTicket() {
		return ticket;
	}

	public void setTicket(GameTicket ticket) {
		this.ticket = ticket;
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
	
}
