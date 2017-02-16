package org.gardler.biglittlechallenge.trials.engine;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gardler.biglittlechallenge.core.AbstractGame;
import org.gardler.biglittlechallenge.core.api.AbstractGameAPI;
import org.gardler.biglittlechallenge.core.api.model.GameTicket;
import org.gardler.biglittlechallenge.core.model.AbstractRounds;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Round;
import org.gardler.biglittlechallenge.core.model.RoundResult;
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
	
	/**
	 * Create a Tournament with the default 2 players.
	 */
	public Tournament() {
		super(2);
	}
	
	/**
	 * Create a tournament for a given moinimum number pof players.
	 */
	public Tournament(int minPlayers) {
		super(minPlayers);
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
    	

    	formula = new LinkedHashMap<String, Double>();
    	formula.put("Dexterity", 1.5);
		formula.put("Intelligence", 0.2);
		formula.put("Strength", 0.2);
		event = new Event("Archery", formula);
    	gameRounds.add(event);
	}

	@Override
	protected RoundResult playRound(Round round) {
		logger.info("Playing round: " + round.getName());
		
		Iterator<GameTicket> itr = getStatus().getTickets().iterator();
		while (itr.hasNext()) {
			GameTicket player = itr.next();
			logger.debug("Requesting cards from " + player.getPlayerName());
			
			// Ask player for cards
			Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFeature.class ));
			WebTarget webTarget = client.target(player.getPlayerEndpoint()).path("player/round");
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.put(Entity.entity(round, MediaType.APPLICATION_JSON));
			if (response.getStatus() != 200) {
				logger.warn("Response from " + player.getPlayerName() + " indicates a lack of success.");
				String msg = response.readEntity(String.class);
				logger.warn(response.getStatus() + " - " + msg);
			}
			
			PlayedCards cards = response.readEntity(PlayedCards.class);
			round.addCards(player, cards);
		}
		
		RoundResult results = round.getResults();
		logger.info("Round is complete. Results:\n" + results.toString());
		return results;
	}

	@Override
	public AbstractGameAPI getAPIEngine() {
		if (apiEngine == null) {
			apiEngine = new TournamentAPI(this);
		}
		return apiEngine;
	}
}
