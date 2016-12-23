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
	private int minNumberOfPlayers;
	protected List<Player> players = new ArrayList<Player>();
	private GameStatus status = new GameStatus();
	protected AbstractGameAPI apiEngine;
	
	public AbstractGame(int minimumNumberOfPlayers) {
		this.setMinimumNumberOfPlayers(minimumNumberOfPlayers);
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
	
	/**
	 * Attempt to add a player to the game. If the game is 'waitingforplayers' then 
	 * the player will be added to the list. If they are the final player needed then
	 * the game is moved to the 'starting' state.
	 * 
	 * If the game is not in the 'waitingforplayers' state then the player is not added
	 * to the game.
	 *  
	 * @param player
	 * @return true if the player is added to the game, false if it is not added
	 */
	public boolean addPlayer(Player player) {
		if (getStatus().getState() == GameStatus.State.WaitingForPlayers) {
			players.add(player);
			logger.debug("Adding player " + players.size() + " to the wite list (" + player.getName() + ")");
			if (players.size() == getMinimumNumberOfPlayers() && getStatus().getState() == GameStatus.State.WaitingForPlayers) {
				logger.debug("We now have enough players to start the game, updating game status to 'starting'");
				getStatus().setState(GameStatus.State.Starting);
			}
			return true;
		} else {
			return false;
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
	 * Get the minimum number of players. In most games this is the number of
	 * players needed before the game will start.
	 * 
	 * @return
	 */
	public int getMinimumNumberOfPlayers() {
		return minNumberOfPlayers;
	}

	/**
	 * Set the minimum number of players. In most games this is the number of
	 * players needed before the game will start. The default is 2 players.
	 */
	public void setMinimumNumberOfPlayers(int minimumNumberOfPlayers) {
		this.minNumberOfPlayers = minimumNumberOfPlayers;
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
	
	public String toString() {
		String result = "a game that requires a minimum of " + getMinimumNumberOfPlayers() + " players\n"; 
		result = result + "It consists of " + gameRounds.size() + " events.\n";
		Iterator<Round> itr = getRounds().iterator();
		while (itr.hasNext()) {
			Round event = (Round)itr.next();
			result = result + "\t" + event.getName() + "\n";
		}
		return result;
	}

	/**
	 * Get the API server that is attached to this Tournament.
	 * @return
	 */
	public abstract AbstractGameAPI getAPIEngine();
		
}
