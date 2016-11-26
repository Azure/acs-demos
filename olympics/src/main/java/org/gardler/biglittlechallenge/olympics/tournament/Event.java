package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Event {

	protected List<Player> players;
	protected Player winner = null;
	protected String name;

	private static Logger logger = LoggerFactory.getLogger(Event.class);

	public Event(String name, List<Player> players) {
		this.name = name;
		this.players = players;
	}

	/**
	 * Play a hand that represents this event.
	 */
	public abstract void playHand();

	/**
	 * Record the details of the winner and ensure the player receives their
	 * winnings.
	 */
	protected void recordWinner(Player winningPlayer) {
		this.winner = winningPlayer;
		if (this.winner != null) {
    		logger.info("Winner of " + this + " is " + this.winner.getName());
    	} else {
    		logger.info(this + " was a draw");
    	}
	}
	
	public String toString() {
		return this.name;
	}
}
