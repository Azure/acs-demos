package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractGame represents a single game. It may consist of one or more rounds.
 *
 */
public abstract class AbstractGame {

	protected AbstractRounds rounds;
	private int desiredNumberOfPlayers = 2;
	protected List<Player> players = new ArrayList<Player>();
	
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
		return rounds.getAsList();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	public List<Player> getPlayers() {
		return players;
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

}
