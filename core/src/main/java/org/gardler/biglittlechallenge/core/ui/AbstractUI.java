package org.gardler.biglittlechallenge.core.ui;

import java.io.Serializable;

import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.PlayerStatus;
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

	/**
	 * When a game is ready to start this method will tell the engine
	 * that the player is ready. This method is part of the game
	 * handshake:
	 * 
	 * 1. Player requests to join a game
	 * 2. Engine response with GameID
	 * 3. Engine waits for enough players to join
	 * 3. Engine tells player game is ready to start
	 * 4. Player tells engine they are ready (this method)
	 * 
	 * If the player confirms they are ready to start then the 
	 * PlayerStatus.state is set to Playing. For convenience the
	 * PlayerStatus object is returned.
	 * 
	 */
	public abstract PlayerStatus startGame(Player player);
}
