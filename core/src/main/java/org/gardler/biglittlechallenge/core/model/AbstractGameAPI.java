package org.gardler.biglittlechallenge.core.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGameAPI {
	private static Logger logger = LoggerFactory.getLogger(AbstractGameAPI.class);
	
	private AbstractGame game;
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	public AbstractGameAPI(AbstractGame game) {
		logger.debug("Creating game api server for " + game.toString());
		this.game = game;
	}
	
	@Path("join")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
	public GameStatus postJoinGame(Player player) {
		logger.info("Player requested to join game: " + player.getName());
		
		if (game.addPlayer(player)) {
			return getStatus();
		} else {
			logger.error("FIXME: return an HTTP response that says \"not available\", player should find a different engine.");
			return null;
		}
	}
	
	@Path("status")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStatus getStatus() {
		return game.getStatus();
	}

	public void abortGame() {
		game.abortGame();
	}

}
