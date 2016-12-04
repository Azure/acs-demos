package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;

/**
 * A hand represents a single hand in a game. This is the cards the player
 * currently holds. It is the cards from which they will play one or more
 * cards at appropriate times in the game.
 * 
 * A Hand is drawn from a Deck.
 *
 */
public class Hand extends Deck implements Serializable {
	private static final long serialVersionUID = 4507943821303911470L;

	public Hand(String name) {
		super(name);
	}
}
