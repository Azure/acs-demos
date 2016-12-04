package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;

/**
 * A Round is a single round in a game.
 *
 */
public class Round {
	private String name;
	private String gameID;
	private ArrayList<Player> playerPositions;
	
	public Round(String name) {
		this.name = name;
	}
	
	public String getGameID() {
		return gameID;
	}
	
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the order of players in the final results.
	 * 
	 * @param playerPositions
	 */
	public void setPlayerPositions(ArrayList<Player> playerPositions) {
		this.playerPositions = playerPositions;
	}
	
	/**
	 * Get an ordered list of players that indicates their final position in the round.
	 */ 
	public ArrayList<Player> getPlayerPositions() {
		return this.playerPositions;
	}
	
}
