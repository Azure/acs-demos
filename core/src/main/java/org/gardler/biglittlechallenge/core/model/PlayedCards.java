package org.gardler.biglittlechallenge.core.model;

/**
 * PlayedCards is a collection of one or more cards that a player is playing in
 * the current round of the game. Players select cards from their Hand and place
 * them into the PlayedCards collection which is then submitted to the game
 * engine for processing.
 *
 */
public class PlayedCards extends Hand {
	private static final long serialVersionUID = -3045556560987629314L;

	public PlayedCards(String name) {
		super(name);
	}

}
