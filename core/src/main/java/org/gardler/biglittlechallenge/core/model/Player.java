package org.gardler.biglittlechallenge.core.model;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;

public abstract class Player {
	
	AbstractUI ui;

	String name;
	Deck deck;
	
	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	/**
	 * Set the user interface to be used by this player.
	 * 
	 * @param ui
	 */
	public void setUI(AbstractUI ui) {
		this.ui = ui;
	}

	/**
	 * Get the user interface to be used by this player.
	 * 
	 * @param ui
	 */
	public AbstractUI getUI() {
		return this.ui;
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
	 * Get the character to be played in a specific event.
	 * @param event
	 * @return
	 */
	public Card getCardForHand(Hand hand) {
		return this.getUI().selectCard(this, hand);
	}
	
	public Player(String name, AbstractUI ui) {
		this.setName(name);
		this.setUI(ui);
	}
	
	public String toString() {
		String result = this.getName();
		result = result + this.getDeck().toString();
		return result;
	}
}
