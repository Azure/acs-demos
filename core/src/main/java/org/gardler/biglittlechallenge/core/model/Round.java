package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Round is a single round in a game.
 *
 */
public class Round {
	public static Logger logger = LoggerFactory.getLogger(Round.class);
	private String name;
	private String gameID;
	private ArrayList<Player> playerPositions;
	protected LinkedHashMap<String, Double> ratingFormula;
	protected HashMap<Player, PlayedCards> cards = new HashMap<Player, PlayedCards>();
	
	public Round() {
		super();
	}
	
	public Round(String name) {
		this.name = name;
	}
	
	public String getGameID() {
		return gameID;
	}
	
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the order of players in the final results.
	 * 
	 * @param playerPositions
	 */
	public void setPlayerPositions(ArrayList<Player> playerPositions) {
		this.playerPositions = playerPositions;
	}
	
	/**
	 * Get an ordered list of players that indicates their final position in the round.
	 */ 
	public ArrayList<Player> getPlayerPositions() {
		return this.playerPositions;
	}

	/**
	 * Record the played cards for a single player.
	 * 
	 * @param player
	 * @param cards
	 */
	public void addCards(Player player, PlayedCards playedCards) {
		this.cards.put(player, playedCards);
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
	 * Calculate a cards rating in this round.
	 */
	private Double calculateRating(Card card) {
		LinkedHashMap<String, Double> formula = getRatingFormula();
		Iterator<String> itr = formula.keySet().iterator();
		Double rating = 0.0;
		while (itr.hasNext()) {
			String key = itr.next();
			Double weight = formula.get(key);
			rating = rating + card.getPropertyAsInteger(key) * weight;
		}
		return rating;
	}

	/**
	 * Calculate a hands rating in this round.
	 */
	public Double calculateRating(PlayedCards cards) {
		Double rating = 0.0;
		Card card;
		Iterator<Card> itr = cards.getCards().values().iterator();
		while (itr.hasNext()) {
			card = itr.next();
			rating = rating + calculateRating(card);
		}
		return rating;
	}	
	
	public Player calculateWinner() {
		Player winner = null;
		Double highestRating = 0.0;
		Iterator<Player> itr = cards.keySet().iterator();
		while (itr.hasNext()) {
			Player player = itr.next();
			Double rating = calculateRating((Card)cards.get(player).getCards().values().toArray()[0]);
			if (highestRating < rating) {
				highestRating = rating;
				winner = player;
			}
		}
		return winner;
	}
}
