package org.gardler.biglittlechallenge.trials.model;

/**
 * Each Player maintains their own Deck in the Trials game. This class
 * represents that Deck. A Deck is the stable of characters, equipment and
 * boosts that can be used during an Event in the Trials.
 * 
 * A Deck is an ordered collection of cards allowing the player to draw from the
 * top of the deck.
 *
 */

public class Deck extends org.gardler.biglittlechallenge.core.model.Deck {

	public Deck(String name) {
		super(name);
	}

	public Character getCharacter(String key) {
		return (Character) getCards().get(key);
	}

}
