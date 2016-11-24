package org.gardler.biglittlechallenge.core.model;

import java.util.HashMap;
import java.util.Iterator;

public class Deck {
	String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	HashMap<String, Card> cards = new HashMap<String, Card>();
	
	public Deck(String name) {
		this.setName(name);
	}

	public HashMap<String, Card> getCards() {
		return cards;
	}
	
	public void addCard(Card card) {
		cards.put(card.getName(), card);
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
