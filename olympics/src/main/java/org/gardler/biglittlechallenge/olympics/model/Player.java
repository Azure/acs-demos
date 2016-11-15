package org.gardler.biglittlechallenge.olympics.model;

public class Player {

	String name;
	Deck deck;
	
	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Player(String name) {
		this.setName(name);
	}
	
}
