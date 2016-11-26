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

	/**
	 * Create a Deck for this player.
	 * @param name the name of the deck
	 */
	public abstract void createDeck(String name);
	
	/**
	 * Select a card to play in the current hand.
	 * @return
	 */
	public abstract Card playCard();

	public Player(String name) {
		this.setName(name);
	}
	
	public String toString() {
		String result = this.getName();
		result = result + this.getDeck().toString();
		return result;
	}
}
