package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;

/**
 * Represents the information about the player and the cards played a given
 * position.
 *
 */
public class RoundPosition implements Serializable {
	private static final long serialVersionUID = 1405410508729663156L;
	Player player;
	PlayedCards cards;
	Double rating;

	/**
	 * Used for deserialization, your application should not call this constructor.
	 */
	protected RoundPosition() {
		super();
	}
	
	public RoundPosition(Player player, PlayedCards cards, Double rating) {
		this.player = player;
		this.cards = cards;
		this.rating = rating;
	}
	
	public Player getPlayer() {
		return player;
	}

	public Double getRating() {
		return this.rating;
	}
	
	/**
	 * get the cards played by this player in this round.
	 * 
	 * @return
	 */
	public PlayedCards getCards() {
		return cards;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(player.getName());
		result.append(" (");
		result.append(getRating());
		result.append(") with ");
		result.append(cards.toString());
		result.append("\n");
		return result.toString();
	}
}
