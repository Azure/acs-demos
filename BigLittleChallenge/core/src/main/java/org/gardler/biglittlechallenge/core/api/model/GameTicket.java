package org.gardler.biglittlechallenge.core.api.model;

import java.io.Serializable;
import java.util.UUID;

import org.gardler.biglittlechallenge.core.model.Player;

public class GameTicket implements Serializable {
	private static final long serialVersionUID = -9130472808155569544L;

	private Integer numberOfPlayers;
	private UUID playerID;
	private String playerName;
	private String playerEndpoint;

	/**
	 * Typically the empty constructor is used for deserialization only.
	 */
	protected GameTicket() {
		super();
	}

	/**
	 * Create a game request object for a given player without specifying a
	 * minimum number of players.
	 * 
	 * @param player
	 */
	public GameTicket(Player player) {
		this.playerID = player.getID();
		this.playerName = player.getName();
		this.playerEndpoint = player.getEndpoint();
		this.numberOfPlayers = null;
	}
	
	/**
	 * Create a game request object for a given player with a specified 
	 * number of players.
	 * 
	 * @param player - player making the request
	 * @param numOfPlayers - number of players
	 */
	public GameTicket(Player player, int numOfPlayers) {
		this(player);
		this.numberOfPlayers = Integer.valueOf(numOfPlayers);
	}

	public Integer getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = Integer.valueOf(numberOfPlayers);
	}

	public UUID getPlayerID() {
		return playerID;
	}

	public void setPlayerID(UUID playerID) {
		this.playerID = playerID;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerEndpoint() {
		return playerEndpoint;
	}

	public void setPlayerEndpoint(String playerEndpoint) {
		this.playerEndpoint = playerEndpoint;
	}

}
