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

import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.trials.model.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("tournament")
public class TournamentAPI {
	private static Logger logger = LoggerFactory.getLogger(TournamentAPI.class);

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
		
		logger.error("FIXME: putJoinGame not implemented yet");
		GameStatus status = getStatus();
		status.setStatus(GameStatus.State.Waiting);
		return status;
	}
	
	@Path("status")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStatus getStatus() {
		logger.error("FIXME: Game status is not really being tracked");
		GameStatus status = new GameStatus();
		status.setStatus(GameStatus.State.Idle);
		return status;
	}
}
