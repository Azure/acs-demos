package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;
import java.util.UUID;

public class GameStatus implements Serializable {
	private static final long serialVersionUID = -2937782814901661605L;
	private UUID gameUUID = UUID.randomUUID();
	private State state = State.WaitingForPlayers;
	
	// WaitingForPlayers means the game is waiting for the minimum number of players before starting
	// Playing means the game is underway 
	public enum State { WaitingForPlayers, Playing }
	
	public UUID getGameUUID() {
		return gameUUID;
	}
	
	/**
	 * Get the current state of the game. See GameStatus.State for allowed values.
	 * @return
	 */
	public State getState() {
		return state;
	}

	/**
	 * Set the current state of the game. See GameStatus.State for allowed values.
	 */
	public void setState(State state) {
		this.state = state;
	}
	
}
