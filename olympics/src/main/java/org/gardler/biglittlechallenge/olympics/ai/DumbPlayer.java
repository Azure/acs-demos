package org.gardler.biglittlechallenge.olympics.ai;

import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Deck;
import org.gardler.biglittlechallenge.olympics.model.Player;

/**
 * DumbPlayer is a simple AI, a very simple AI. It always plays a random card regardless of the circumstances.
 * 
 */
public class DumbPlayer extends Player {
	
	public DumbPlayer(String name) {
		super(name, new DumbAIUI());
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
