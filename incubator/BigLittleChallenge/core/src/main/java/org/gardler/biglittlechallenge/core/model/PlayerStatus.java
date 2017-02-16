package org.gardler.biglittlechallenge.core.model;

public class PlayerStatus {

	private String gameUID;
	private String playerID;
	private State state = State.Idle;
	/**
	 * Idle: Not doing anything
	 * Requesting: Finding a game to join
	 * Waiting: Joined a game, waiting for game to start
	 * Ready: Game is ready to start, have indicated that player is ready
	 * Playing: Game is underway
	 */
	public enum State { Idle, Requesting, Waiting, Ready, Playing }
	
	public String getGameUID() {
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public String getPlayerID() {
		return playerID;
	}
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	public State getState() {
		return state;
	}
	public void setState(State status) {
		this.state = status;
	}
	
}
