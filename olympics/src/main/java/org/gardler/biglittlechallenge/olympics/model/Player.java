package org.gardler.biglittlechallenge.olympics.model;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;

/**
 * Player represents a player in a game, this may be an AI or a human player.
 *
 */
public class Player extends org.gardler.biglittlechallenge.core.model.Player {

	public Player(String name, AbstractUI ui) {
		super(name, ui);		
		createDeck(name + "'s Deck");
	}

	@Override
	public void createDeck(String name) {
		Deck deck = new Deck(name);
    	Character card = new Character("Foo");
    	deck.addCard(card);
    	card = new Character("Potato");
    	deck.addCard(card);
    	this.setDeck(deck);
	}

}
