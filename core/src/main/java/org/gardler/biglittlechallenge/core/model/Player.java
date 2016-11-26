package org.gardler.biglittlechallenge.core.model;

public abstract class Player {

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

	public abstract void createDeck(String name);

	public Player(String name) {
		this.setName(name);
	}
	
}
