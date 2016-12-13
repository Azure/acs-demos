package org.gardler.biglittlechallenge.trials.model;

public class PlayerStatus {

	private String gameUID;
	private String playerID;
	private State status;
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
	public State getStatus() {
		return status;
	}
	public void setStatus(State status) {
		this.status = status;
	}
	
}
