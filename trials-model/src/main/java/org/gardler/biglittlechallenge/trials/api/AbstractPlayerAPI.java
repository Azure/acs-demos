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
import org.gardler.biglittlechallenge.core.model.PlayerStatus;
import org.gardler.biglittlechallenge.core.model.Round;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPlayerAPI {
	private static Logger logger = LoggerFactory.getLogger(AbstractPlayerAPI.class);
	protected Player player;

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	public AbstractPlayerAPI(Player player) {
		this.player = player;
	}

	@Path("status")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PlayerStatus getStatus() {
		logger.info("REST Service Method getGameStatus called");

		PlayerStatus status = player.getStatus();
		return status;
	}

	@Path("status")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public PlayerStatus putGameStatus(PlayerStatus newStatus) {
		logger.info("REST Service Method putGameStatus called");
		
		if (newStatus.getState() == PlayerStatus.State.Ready) {
			player.ui().startGame(this.player);
		}
		PlayerStatus status = newStatus;
		return status;
	}
	
	@Path("event")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public PlayedCards getCardsForRound(Round round) {
		return player.getCardsForHand(round);
	}

	@Path("round/result")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public PlayerStatus postResult(Round round) {
		// TODO: Auto-generated method stub
		return player.getStatus();
	}

}