package org.gardler.biglittlechallenge.core.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gardler.biglittlechallenge.core.model.PlayerStatus.State;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Player implements Serializable {
	private static final long serialVersionUID = -6951818858527928715L;

	private static Logger logger = LoggerFactory.getLogger(Player.class);

	private static PlayerStatus status = new PlayerStatus();
	private static String engineEndpoint = null;
	private String playerEndpoint;
	
	String name;
	Deck deck;
	UUID id;

	private String uiClassName = "UNDEFINED";
	
	/**
	 * Default constructor to enable this class to be deserialized. 
	 * Should not be used in game engines. 
	 */
	protected Player() {
	}
	
	/**
	 * Create a player.
	 * 
	 * @param name
	 * @param uiClassName
	 */
	public Player(String name, String uiClassName) {
		super();
		this.setName(name);
		this.uiClassName = uiClassName;
		this.id = UUID.randomUUID();
	}
	
	public Deck getDeck() {
		if (deck == null) {
			createDeck(name + "'s Deck");
		}
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Create a Deck for this player.
	 * 
	 * @param name the name of the deck
	 */
	public void createDeck(String name) {
		this.setDeck(ui().createDeck(this));
	}
	

	/**
	 * Get the cards to be played in a specific Round
	 * @return
	 */
	public PlayedCards getCardsForHand(Round round) {
		return ui().selectCards(this, round);
	}
	
	/**
	 * Save the Player object so that we can load it dynamically at
	 * application startup.
	 * @throws IOException 
	 */
	public void save() throws IOException {
          FileOutputStream fileOut = new FileOutputStream("test_1.player");
          ObjectOutputStream out = new ObjectOutputStream(fileOut);
          out.writeObject(this);
          out.close();
          fileOut.close();
          logger.info("Serialized Player in `test_1.player`");
	}
	
	public static AbstractRounds load() throws IOException, ClassNotFoundException {
		AbstractRounds hands = null;
        FileInputStream fileIn = new FileInputStream("test_1.player");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        hands = (AbstractRounds) in.readObject();
        in.close();
        fileIn.close();
        logger.info("Loaded Player definition file from `test_1.player`");
        return hands;
	}
	
	public String toString() {
		String result = this.getName();
		result = result + this.getDeck().toString();
		return result;
	}
	
	/**
	 * Request to join a game.
	 * 
	 * This sends a request to the game engine (via REST API at
	 * `engineEndpoint`) to join a game. When a game is ready the engine will
	 * call back to the player via the player API and the game can start.
	 * 
	 */
	public void joinTournament(String engineEndpoint) {
		this.getStatus().setState(PlayerStatus.State.Requesting);
		Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
		WebTarget webTarget = client.target(engineEndpoint).path("api/v0.1/tournament/join");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(Entity.entity(this, MediaType.APPLICATION_JSON));
		
		GameStatus status = response.readEntity(GameStatus.class);
		/*
		 * FIXME: Saw this intermittent error
		 * 1705 [main] DEBUG org.gardler.biglittlechallenge.trials.ai.AiPlayerApp  - Waiting for 0ms before requesting to join a new game.
         * 1948 [main] INFO  org.gardler.biglittlechallenge.trials.ai.DumbAIUI  - Created Dumb AI Player UI
         * Exception in thread "main" java.lang.NullPointerException
         *      at org.gardler.biglittlechallenge.core.model.Player.joinTournament(Player.java:146)
         *	    at org.gardler.biglittlechallenge.trials.ai.AiPlayerApp.main(AiPlayerApp.java:64)
		 * 
		 */
		if (status.getState() == GameStatus.State.WaitingForPlayers
				|| status.getState() == GameStatus.State.Starting) {
			this.getStatus().setState(PlayerStatus.State.Waiting);
		} else 
		
		logger.debug("Request to join tournament - response (status " + response.getStatus() + "): " + status);
	}

	public PlayerStatus getStatus() {
		if (status == null) {
			status = new PlayerStatus();
			status.setPlayerID(getID().toString());
			status.setState(PlayerStatus.State.Idle);
		}
		return status;
	}
	
	public void setStatus(PlayerStatus status) {
		Player.status = status;
	}
	
	public UUID getID() {
		return id;
	}
	
	public void setID(UUID id) {
		this.id = id;
	}
	
	public AbstractUI ui() {
		Class<?> cls;
		AbstractUI ui = null;
		try {
			cls = Class.forName(uiClassName);
			ui = (AbstractUI) cls.newInstance();
		} catch (ClassNotFoundException e) {
			String msg = "Cannot find the UI class: " + uiClassName + ". Please ensure that you set the correct classname when instantiating the Player or through a call to setUIClassName()";
			logger.error(msg, e);
			System.exit(0);
		} catch (InstantiationException e) {
			String msg = "Cannot instantiate the UI class: " + uiClassName + ". Please ensure that you set the correct classname when instantiating the Player or through a call to setUIClassName()";
			logger.error(msg, e);
			System.exit(0);
		} catch (IllegalAccessException e) {
			String msg = "Cannot access the UI class: " + uiClassName + ". Please ensure that you set the correct classname when instantiating the Player or through a call to setUIClassName()";
			logger.error(msg, e);
			System.exit(0);
		}
		return ui;
	}
	
	/**
	 * Get the class name for the UI for this player. 
	 * This is used to instantiate the UI class at runtime.
	 * By default it will not return a valid class name.
	 * It is the responsibility of game engine to set this
	 * correctly through the Player constructor or through
	 * a call to setUIClassname.
	 * 
	 * @return Class name of the UI for this player
	 */
	public String getUIClassName() {
		return uiClassName;
	}
	
	/**
	 * Set the name of the class that provides the UI for tihs player.
	 * 
	 * @param className
	 */
	public void setUIClassName(String className) {
		this.uiClassName = className;
	}

	public String getEndpoint() {
		return playerEndpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.playerEndpoint = endpoint;
	}

	/**
	 * Call this when the current game has finished.
	 * @param result
	 */
	public void gameFinished(GameResult result) {
		logger.info("Player informed that the game has finished");
		logger.info(result.toString());
		PlayerStatus status = this.getStatus();
		status.setState(State.Idle);
	}
	
	/**
	 * Return the endpoint of an available engine API.
	 * 
	 * @return
	 * @throws EngineNotFoundException
	 */
	public static String getEngineEndoint() {
		int index = 0;
		String[] url = { "http://localhost:8080/", "http://engine:8080/" };
		
		while (engineEndpoint == null) { 
			Response response = checkEngineStatus(url[index]);
			if (response != null) {
				if (response.getStatus() != 200) {
					logger.warn("Got a response other than 200 from Engine at " + url + " - " + response.getStatus());
				} else {
					engineEndpoint = url[index];
				}
			}
			
			index = index + 1;
			if (index >= url.length) {
				index = 0;
				logger.warn("Unable to connect to an available engine, waiting then trying again");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.warn("Got interupted while sleeping");
				}
			}
		}
		
		logger.debug("Connected to engine at " + engineEndpoint);
		return engineEndpoint;
	}

	/**
	 * Attempts to connect to an Engine API. Returns null if no response or the
	 * response object containing the response to a GET of the current status.
	 * 
	 * @param statusPath
	 * @param url
	 * @return
	 */
	protected static Response checkEngineStatus(String url) {
		String statusPath = "api/v0.1/tournament/status";
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget webTarget = client.target(url).path(statusPath);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		try {
			Response response = invocationBuilder.get();
			logger.debug("Engine response recieved from " + url + statusPath + " : " + response.readEntity(String.class));
			return response;
		} catch (Exception e) {
			logger.debug("Unable to get engine status from " + url + statusPath + " - " + e.getMessage());
			return null;
		}
	}
}
