package org.gardler.biglittlechallenge.core.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

	transient AbstractUI ui;

	String name;
	Deck deck;
	
	public Player() {
		this.setName("Default Player");
	}
	
	public Player(String name, AbstractUI ui) {
		this.setName(name);
		this.ui = ui;
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
		this.setDeck(ui.createDeck(this));
	}
	

	/**
	 * Get the cards to be played in a specific Round
	 * @return
	 */
	public PlayedCards getCardsForHand(Round round) {
		return ui.selectCards(this, round);
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
}
