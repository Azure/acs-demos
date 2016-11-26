package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Character;
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
     * Play the event (hand).
     * 
     * @param players
     */
	protected void playHand() {
		List <Character> characters = new ArrayList<Character>();
		List <Integer> ratings = new ArrayList<Integer>();
		
		int numOfPlayers = players.size();
		for (int i = 0; i < numOfPlayers; i++) {
			Character character = players.get(i).playCharacter();
			characters.add(character);
			Integer rating = calculateRating(character);
			ratings.add(rating);
		}
		    	
    	Player winner = null;
    	Integer highestRating = 0;
    	for (int i = 0; i < numOfPlayers; i++ ) {
    		if (highestRating < ratings.get(i)) {
    			highestRating = ratings.get(i);
    			winner = players.get(i);
    		}
    	}

    	recordWinner(winner);
	}
	
	/**
	 * Calculate a characters rating in this event.
	 */
	protected abstract Integer calculateRating(Character character);

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
