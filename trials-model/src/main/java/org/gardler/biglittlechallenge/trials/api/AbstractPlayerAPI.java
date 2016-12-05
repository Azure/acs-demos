package org.gardler.biglittlechallenge.trials.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.Round;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.gardler.biglittlechallenge.trials.model.PlayerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPlayerAPI {
	private static Logger logger = LoggerFactory.getLogger(AbstractPlayerAPI.class);
	private static PlayerStatus status = new PlayerStatus();
	protected Player player;
	    
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	public AbstractPlayerAPI(Player player, AbstractUI ui) {
		this.player = player;
	}
	
	@Path("status")
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public PlayerStatus getGameStatus() {
    	logger.info("REST Service Method putGameStatus called");
    	
    	PlayerStatus status = getStatus();
        return status;
    }

    @Path("status")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
    public PlayerStatus putGameStatus(PlayerStatus newStatus) {
    	logger.info("REST Service Method putGameStatus called");
    	
    	PlayerStatus status = newStatus;
        return status;
    }
    
    @Path("event")
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public PlayedCards getCardsForRound(Round round) {
    	return player.getCardsForHand(round);
    }

    private PlayerStatus getStatus() {
		status.setGameUID("gameUID");
    	status.setPlayerID("playerUID");
    	status.setStatus(PlayerStatus.State.Idle);
		return status;
	}

    @Path("round/result")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
    public PlayerStatus postResult(Round round) {
		status.setGameUID(round.getGameID());
    	status.setPlayerID("playerUID");
    	status.setStatus(PlayerStatus.State.Playing);
		return status;
	}

}