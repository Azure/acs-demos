package org.gardler.biglittlechallenge.core.ui;

import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.Hand;

/**
 * Provides the core UI class that allows objects to interact with the user.
 *
 */
public abstract class AbstractUI {
	
	/**
	 * Get a player to select a card to play in a particular hand.
	 * 
	 * @param player
	 * @param hand
	 */
	public abstract PlayedCards selectCards(Player player, Hand hand);

	/**
	 * Create a deck for a player.
	 * 
	 * @param player
	 */
	public abstract Deck createDeck(Player player);
}
