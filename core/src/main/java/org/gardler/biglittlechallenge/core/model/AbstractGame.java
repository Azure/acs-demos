package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gardler.biglittlechallenge.core.model.GameStatus.State;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
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
	protected String name = "Game with no name (FIXME: no logic for setting name";

	public AbstractGame(int minimumNumberOfPlayers) {
		this.setMinimumNumberOfPlayers(minimumNumberOfPlayers);
		this.reset();
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
		logger.debug("Trying to add " + player.getName() + " to game " + this.getName());
		if (players.size() < getMinimumNumberOfPlayers()) {
			players.add(player);
			logger.debug("Now have " + players.size() + " of " + getMinimumNumberOfPlayers() + " players. Just added (" + player.getName() + ")");
			return true;
		} else {
			logger.debug("The game is full");
			return false;
		}
	}

	private void playGame() {
		logger.info("Starting the game");
		getStatus().setResults(new GameResult());
		
		getStatus().setState(GameStatus.State.Playing);
		
		// Play all rounds
		Iterator<Round> itr = gameRounds.rounds.iterator();
		while (itr.hasNext()) {
			Round round = itr.next();
			playRound(round);
			getStatus().getResults().addRound(round);
		}
		logger.info("Game is complete.");
		logger.info(getStatus().getResults().toString());

		getStatus().setState(GameStatus.State.Finishing);
	}
	
	/**
	 * Play a round in this game. Implementations should liase with players to 
	 * complete the round and should then return the results for further processing.
	 * 
	 * @param round
	 * @return RoundResult
	 */
	protected abstract RoundResult playRound(Round round);

	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Abort the current game and reset the game status.
	 */
	public void abortGame() {
		logger.warn("FIXME: tell players the game has been aborted.");
		this.reset();
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
		logger.info("Starting game control loop");
		while(true) {
			// If we are ready to start then lets get on with it
			status = this.getStatus();
			switch (status.getState()) {
			case WaitingForPlayers:
				// logger.info("Waiting for players before we can start the game");
				if (players.size() == getMinimumNumberOfPlayers()) {	
					logger.debug("We now have enough players to start the game, updating game status to 'starting'");
					getStatus().setState(GameStatus.State.Starting);
				}
				break;
			case Starting:
				// Notify all players that we are ready to start
				Iterator<Player> itr = this.getPlayers().iterator();
				while (itr.hasNext()) {
					// TODO: Parallelize getting responses from players
					Player player = itr.next();
					
					Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
					WebTarget webTarget = client.target(player.getEndpoint()).path("player/readyToStart");
					Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
					Response response = invocationBuilder.get();
					if (response.getStatus() != 200) {
						// TODO: Handle situation where a player is unreachable/does not respond
						logger.warn("Response from " + player.getName() + " indicates a lack of success.");
						String msg = response.readEntity(String.class);
						logger.warn(response.getStatus() + " - " + msg);
					} else {
						// TODO: Handle situation where a player is unreachable/does not respond
						logger.info(player.getName() + " is ready to start playing");	
					}
				}
				
				this.playGame();
			case Playing:
				// Nothing to do here as it's all driven in playGame()
				break;
			case Finishing:
				logger.debug("Tidying up after the game");
				
				// Notify all players that the game is complete
				Iterator<Player> playersItr = this.getPlayers().iterator();
				while (playersItr.hasNext()) {
					Player player = playersItr.next();
					
					Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
					WebTarget webTarget = client.target(player.getEndpoint()).path("player/finishGame");
					Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
					Response response = invocationBuilder.put(Entity.entity(getStatus().getResults(), MediaType.APPLICATION_JSON));
					if (response.getStatus() != 200) {
						// TODO: Handle situation where a player is unreachable/does not respond
						logger.warn("Response from " + player.getName() + " indicates a lack of success.");
						String msg = response.readEntity(String.class);
						logger.warn(response.getStatus() + " - " + msg);
					} else {
						// TODO: Handle situation where a player is unreachable/does not respond
						logger.info(player.getName() + " confirms the game has ended.");	
					}
				}
				
				reset();
				
				break;
			default:
				logger.warn("Game status is unknown" + status);
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reset the game ready for a new game.
	 */
	private void reset() {
		this.players = new ArrayList<Player>();
		getStatus().setResults(new GameResult());
		this.setRounds();
		this.getStatus().setState(State.WaitingForPlayers);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String result = getName();
		result = result + "a game that requires a minimum of " + getMinimumNumberOfPlayers() + " players\n"; 
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
