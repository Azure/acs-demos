package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractGame represents a single game. It may consist of one or more rounds.
 *
 */
public abstract class AbstractGame implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(AbstractGame.class);
			
	protected AbstractRounds gameRounds;
	private int desiredNumberOfPlayers = 2;
	protected List<Player> players = new ArrayList<Player>();
	private GameStatus status = new GameStatus();
	protected AbstractGameAPI apiEngine;
	
	public AbstractGame(List<Player> players) {
		this.players = players;
		this.setRounds();
        Thread t = new Thread(this);
        t.start();
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
			getStatus().setState(GameStatus.State.Starting);
		}
		
	}

	private void playGame() {
		logger.info("Starting the game");
		getStatus().setState(GameStatus.State.Playing);
		Iterator<Round> itr = gameRounds.rounds.iterator();
		while (itr.hasNext()) {
			Round round = itr.next();
			playRound(round);
		}
		logger.info("Game is complete");
		getStatus().setState(GameStatus.State.Finishing);
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
	
	/**
	 * A control loop for the game.
	 */
	public void run() {
		logger.info("Starting game contorol loop");
		while (true) {
			if (getStatus().getState() == GameStatus.State.Starting) {
				this.playGame();
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the API server that is attached to this Tournament.
	 * @return
	 */
	public abstract AbstractGameAPI getAPIEngine();
		
}
