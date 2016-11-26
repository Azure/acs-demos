package org.gardler.biglittlechallenge.olympics.ai;

import java.util.Random;
import java.util.Set;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Deck;
import org.gardler.biglittlechallenge.olympics.model.Player;

/**
 * DumbPlayer is a simple AI, a very simple AI. It always plays a random card regardless of the circumstances.
 * 
 */
public class DumbPlayer extends Player {

	public DumbPlayer(String name) {
		super(name);
		
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

	@Override
	public Card playCard() {
		Deck deck = (Deck)this.getDeck();
		Set<String> keys = this.getDeck().getCards().keySet();

		Random rand = new Random();
		int idx = rand.nextInt(keys.size());
		String key = (String) keys.toArray()[idx];
		return  deck.getCharacter(key);
	}
	

}
