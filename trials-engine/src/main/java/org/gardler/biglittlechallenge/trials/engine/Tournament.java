package org.gardler.biglittlechallenge.trials.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gardler.biglittlechallenge.core.model.AbstractGame;
import org.gardler.biglittlechallenge.core.model.AbstractGameAPI;
import org.gardler.biglittlechallenge.core.model.AbstractRounds;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.Round;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Tournament is a single game of Trials. It consists of a number of events.
 * Players will enter each of the events in a tournament.
 * 
 */
public class Tournament extends AbstractGame {
	
	private static Logger logger = LoggerFactory.getLogger(Tournament.class);
	
	public Tournament() {
		super(new ArrayList<Player>());
		this.setRounds();
	}
	
	/**
	 * Set the Rounds to be played in this tournament.
	 */
	protected void setRounds() {
		try {
			gameRounds = AbstractRounds.load();
			if (gameRounds != null) return;
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		} catch (IOException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		}
		gameRounds = new Rounds();
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Speed", 1.0);
		formula.put("Reactions", 0.5);
		Round event = new Event("Track: 100m Sprint", formula);
    	gameRounds.add(event);
    	
		formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		event = new Event("Track: 8000m", formula);
    	gameRounds.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		formula.put("Dexterity", 1.0);
		event = new Event("Track: 8000m Steeple Chase", formula);
    	gameRounds.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
    	formula.put("Dexterity", 1.0);
		formula.put("Strength", 0.5);
		event = new Event("Field: Pole Vault", formula);
    	gameRounds.add(event);
	}
	
	public String toString() {
		String result = "This tournament consists of " + gameRounds.size() + " events.\n";
		Iterator<Round> itr = getRounds().iterator();
		while (itr.hasNext()) {
			Round event = (Round)itr.next();
			result = result + "\t" + event.getName() + "\n";
		}
		return result;
	}

	@Override
	protected void playRound(Round round) {
		logger.info("Playing round: " + round.getName());
		
		Iterator<Player> itr = players.iterator();
		while (itr.hasNext()) {
			Player player = itr.next();
			
			// Ask player for cards
			Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
			WebTarget webTarget = client.target(player.getEndpoint()).path("player/round");
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.put(Entity.entity(round, MediaType.APPLICATION_JSON));
			
			PlayedCards cards = response.readEntity(PlayedCards.class);
			round.addCards(player, cards);
		}
		
		logger.info("Winner of " + round.getName() + " is " + round.calculateWinner().getName());
	}

	@Override
	public AbstractGameAPI getAPIEngine() {
		if (apiEngine == null) {
			apiEngine = new TournamentAPI(this);
		}
		return apiEngine;
	}
}
