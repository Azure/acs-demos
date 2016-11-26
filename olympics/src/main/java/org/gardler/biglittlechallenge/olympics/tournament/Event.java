package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Event {

	protected Player winner = null;
	protected String name;
	private LinkedHashMap<String, Double> ratingFormula;

	private static Logger logger = LoggerFactory.getLogger(Event.class);

	/**
	 * Create a new Event.
	 * 
	 * @param name of the event
	 * @param formula for calculating the ratings of characters in this event (see getRatingForumla)
	 * @param players the players that will participate in this event
	 */
	public Event(String name, LinkedHashMap<String, Double> formula) {
		this.name = name;
		this.ratingFormula = formula;
	}
	
    /**
     * Play the event (hand).
     * 
     * @param players
     */
	protected void playHand(List<Player> players) {
		List <Character> characters = new ArrayList<Character>();
		List <Double> ratings = new ArrayList<Double>();
		
		int numOfPlayers = players.size();
		for (int i = 0; i < numOfPlayers; i++) {
			Character character = players.get(i).playCharacter();
			characters.add(character);
			Double rating = calculateRating(character);
			ratings.add(rating);
		}
		    	
    	Player winner = null;
    	Double highestRating = 0.0;
    	for (int i = 0; i < numOfPlayers; i++ ) {
    		if (highestRating < ratings.get(i)) {
    			highestRating = ratings.get(i);
    			winner = players.get(i);
    		}
    	}

    	recordWinner(winner);
	}
	
	/**
	 * ratingFormula is a representation of the calculation used to calculate
	 * the winner. It consists a number of key value pairs of the form <Trait,
	 * Weight>. Trait is the name of the trait to use while the Weight is a
	 * float value indicating the weight of the trait in the calculation.
	 * 
	 * The weight can be positive or negative and is a double. For example, a
	 * sprint race may have a rating formula of:
	 * 
	 * <"Speed", 1.0>
	 * <"Reactions", 0.5>
	 */
	protected LinkedHashMap<String, Double> getRatingFormula() {
		return this.ratingFormula;
	}
	
	/**
	 * Calculate a characters rating in this event.
	 */
	protected Double calculateRating(Character character) {
		LinkedHashMap<String, Double> formula = getRatingFormula();
		Iterator<String> itr = formula.keySet().iterator();
		Double rating = 0.0;
		while (itr.hasNext()) {
			String key = itr.next();
			Double weight = formula.get(key);
			rating = rating + character.getPropertyAsInteger(key) * weight;
		}
		return rating;
	}

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
