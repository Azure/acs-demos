package org.gardler.biglittlechallenge.core.ui;

import java.io.Serializable;

import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.Round;

/**
 * Provides the core UI class that allows objects to interact with the user.
 *
 */
public abstract class AbstractUI implements Serializable {
	private static final long serialVersionUID = 986571168724595733L;

	/**
	 * Get a player to select a card to play in a particular Round.
	 * 
	 * @param player
	 * @param round
	 */
	public abstract PlayedCards selectCards(Player player, Round round);

	/**
	 * Create a deck for a player.
	 * 
	 * @param player
	 */
	public abstract Deck createDeck(Player player);
}
