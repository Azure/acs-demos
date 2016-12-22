package org.gardler.biglittlechallenge.core.model;

public class PlayerStatus {

	private String gameUID;
	private String playerID;
	private State state = State.Idle;
	public enum State { Idle, Waiting, Ready, Playing }
	
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
