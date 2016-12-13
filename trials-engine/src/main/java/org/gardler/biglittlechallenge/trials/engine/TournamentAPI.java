package org.gardler.biglittlechallenge.trials.engine;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.gardler.biglittlechallenge.core.model.AbstractGame;
import org.gardler.biglittlechallenge.core.model.GameStatus;
import org.gardler.biglittlechallenge.core.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("tournament")
public class TournamentAPI {
	private static Logger logger = LoggerFactory.getLogger(TournamentAPI.class);
	private static AbstractGame tournament = new Tournament();
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Path("join")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
	public GameStatus postJoinGame(Player player) {
		logger.info("Player requested to join game: " + player.getName());
		
		tournament.addPlayer(player);
		
		return getStatus();
	}
	
	@Path("status")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStatus getStatus() {
		if (tournament.getPlayers().size() == tournament.getDesiredNumberOfPlayers() && tournament.getStatus().getState() == GameStatus.State.WaitingForPlayers) {
			tournament.getStatus().setState(GameStatus.State.Playing);
		}
		return tournament.getStatus();
	}

	public void abortGame() {
		tournament.abortGame();
	}
}
