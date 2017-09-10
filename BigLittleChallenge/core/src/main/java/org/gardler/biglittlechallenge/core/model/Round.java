package org.gardler.biglittlechallenge.core.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.gardler.biglittlechallenge.core.api.model.GameTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Round is a single round in a game.
 *
 */
public class Round {
	public static Logger logger = LoggerFactory.getLogger(Round.class);
	String name;
	String gameID;
	RoundResult results = new RoundResult();
	protected LinkedHashMap<String, Double> ratingFormula;
	protected HashMap<GameTicket, PlayedCards> cards = new HashMap<GameTicket, PlayedCards>();
	
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
	 * Record the played cards for a single player.
	 * 
	 * @param player
	 * @param cards
	 */
	public void addCards(GameTicket player, PlayedCards playedCards) {
		this.cards.put(player, playedCards);

		Double rating = this.calculateRating(playedCards);
		this.results.addResult(player, playedCards, rating);
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
	
	public GameTicket calculateWinner() {
		GameTicket winner = null;
		Double highestRating = 0.0;
		Iterator<GameTicket> itr = cards.keySet().iterator();
		while (itr.hasNext()) {
			GameTicket player = itr.next();
			Double rating = calculateRating((Card)cards.get(player).getCards().values().toArray()[0]);
			if (highestRating < rating) {
				highestRating = rating;
				winner = player;
			}
		}
		return winner;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Round: ");
		sb.append(this.getName());
		sb.append(" in ");
		sb.append(this.getGameID());
		sb.append("\n");
		
		sb.append(results.toString());
		
		return sb.toString();
	}

	public RoundResult getResults() {
		return this.results;
	}
}
