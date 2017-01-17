package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gardler.biglittlechallenge.core.api.model.GameTicket;

public class GameStatus implements Serializable {
	private static final long serialVersionUID = -2937782814901661605L;
	private UUID gameUUID = UUID.randomUUID();
	private State state = State.WaitingForPlayers;
	private GameResult gameResults;
	private List<GameTicket> tickets = new ArrayList<GameTicket>();
	private int minNumberOfPlayers;

	// WaitingForPlayers means the game is waiting for the minimum number of
	// players before starting
	// Playing means the game is underway
	public enum State {
		Idle, WaitingForPlayers, Starting, Playing, Finishing
	}

	public UUID getGameUUID() {
		return gameUUID;
	}

	/**
	 * Get the player tickets that have been issued for this game. That is get a
	 * list of players expected to participate.
	 * 
	 * @return list of GameTickets
	 */
	public List<GameTicket> getTickets() {
		return tickets;
	}
	
	/**
	 * Get the current state of the game. See GameStatus.State for allowed
	 * values.
	 * 
	 * @return
	 */
	public State getState() {
		return state;
	}

	/**
	 * Set the current state of the game. See GameStatus.State for allowed
	 * values.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Set the results of either the in progress game (when getState() ==
	 * State.Playing) or the results of the most recently played game.
	 */
	public void setResults(GameResult results) {
		this.gameResults = results;
	}

	/**
	 * Get the results of either the in progress game (when getState() ==
	 * State.Playing) or the results of the most recently played game.
	 */
	public GameResult getResults() {
		return gameResults;
	}

	/**
	 * Reset the game status for a new game.
	 */
	public void reset() {
		tickets = new ArrayList<GameTicket>();
		gameResults = new GameResult();
	}

	public int getMinNumberOfPlayers() {
		return minNumberOfPlayers;
	}

	public void setMinNumberOfPlayers(int minNumberOfPlayers) {
		this.minNumberOfPlayers = minNumberOfPlayers;
	}
}
