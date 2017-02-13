package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A Deck is a collection of cards available to one or more players in a game.
 * When a Card is drawn from the Deck it is usually placed into a Hand before
 * being played in the game.
 *
 */
public class Deck implements Serializable {
	private static final long serialVersionUID = 4417178470398581230L;

	String name;

	public Deck() {
		this.setName("Default Deck");
	}
	
	public Deck(String name) {
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	HashMap<String, Card> cards = new HashMap<String, Card>();

	public HashMap<String, Card> getCards() {
		return cards;
	}

	public Card getCard(String key) {
		return cards.get(key);
	}

	public void addCard(Card card) {
		cards.put(card.getName(), card);
	}

	/**
	 * Return the number of cards in this Hand.
	 * @return
	 */
	public int size() {
		return cards.size();
	}

	public String toString() {
		String result = "";
		Iterator<Card> itr = getCards().values().iterator();
		while (itr.hasNext()) {
			Card card = itr.next();
			result = result + "\n" + card.toString();
		}
		return result;
	}

}
