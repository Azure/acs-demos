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

import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Player implements Serializable {
	private static final long serialVersionUID = -6951818858527928715L;

	private static Logger logger = LoggerFactory.getLogger(Player.class);

	private static PlayerStatus status = new PlayerStatus();
	private String endpoint;
	
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
	 * Create a player with default settings that will use the
	 * class defined as the players UI.
	 * 
	 * @param uiClassName
	 */
	public Player(String uiClassName) {
		this.setName("Default Player");
		this.setStatus(new PlayerStatus());
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
		Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
		WebTarget webTarget = client.target(engineEndpoint).path("api/v0.1/tournament/join");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(Entity.entity(this, MediaType.APPLICATION_JSON));
		
		logger.debug("Request to join tournament - response (status " + response.getStatus() + "): " + response.readEntity(String.class));
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
		return endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}
