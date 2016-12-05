package org.gardler.biglittlechallenge.trials.model;

public class GameStatus {

	private String gameUID;
	private State status;
	public enum State { Idle, Waiting, Ready, Playing }
	
	public String getGameUID() {
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public State getStatus() {
		return status;
	}
	public void setStatus(State status) {
		this.status = status;
	}
	
}
