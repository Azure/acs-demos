package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractGame represents a single game. It may consist of one or more rounds.
 *
 */
public abstract class AbstractGame {

	protected AbstractRounds gameRounds;
	private int desiredNumberOfPlayers = 2;
	protected List<Player> players = new ArrayList<Player>();
	private GameStatus status = new GameStatus();
	
	public AbstractGame(List<Player> players) {
		this.players = players;
		this.setRounds();
	}
	
	/**
	 * Set the hands to be played in this game.
	 */
	protected abstract void setRounds();
	
	/**
	 * Get an ordered List of the hands to be played in this game.
	 */
	public ArrayList<Round> getRounds() {
		return gameRounds.getAsList();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		if (players.size() == getDesiredNumberOfPlayers() && getStatus().getState() == GameStatus.State.WaitingForPlayers) {
			playGame();
		}
		
	}

	private void playGame() {
		getStatus().setState(GameStatus.State.Playing);
		Iterator<Round> itr = gameRounds.rounds.iterator();
		while (itr.hasNext()) {
			Round round = itr.next();
			playRound(round);
		}
	}
	
	protected abstract void playRound(Round round);

	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Abort the current game and reset the game status.
	 */
	public void abortGame() {
		status.setState(GameStatus.State.WaitingForPlayers);
		players = new ArrayList<Player>();
	}
	
	/**
	 * Get the desired number of players. In most games this is the number of
	 * players needed before the game will start.
	 * 
	 * @return
	 */
	public int getDesiredNumberOfPlayers() {
		return desiredNumberOfPlayers;
	}

	/**
	 * Set the desired number of players. In most games this is the number of
	 * players needed before the game will start. The default is 2 players.
	 */
	public void setDesiredNumberOfPlayers(int desiredNumberOfPlayers) {
		this.desiredNumberOfPlayers = desiredNumberOfPlayers;
	}

	public GameStatus getStatus() {
		if (status == null) {
			status = new GameStatus();
		}
		return status;
	}

}
