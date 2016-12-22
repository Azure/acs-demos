package org.gardler.biglittlechallenge.trials.engine;

import javax.ws.rs.Path;

import org.gardler.biglittlechallenge.core.model.AbstractGame;
import org.gardler.biglittlechallenge.core.model.AbstractGameAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("tournament")
public class TournamentAPI extends AbstractGameAPI {
	private static Logger logger = LoggerFactory.getLogger(TournamentAPI.class);
	private static AbstractGame tournament = new Tournament();
	
	public TournamentAPI(Tournament tournament) {
		super(tournament);
		logger.debug("Created a TournamentAPI for " + tournament);
	}

}
