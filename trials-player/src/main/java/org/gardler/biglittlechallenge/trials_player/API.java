package org.gardler.biglittlechallenge.trials_player;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.gardler.biglittlechallenge.trials_player.model.PlayerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Player API is used to communicate with the player.
 *
 */
@Path("player")
public class API {
	private static Logger logger = LoggerFactory.getLogger(API.class);
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("status")
    public PlayerStatus putGameStatus() {
    	logger.info("REST Service Method putGameStatus called");
    	
    	PlayerStatus status = new PlayerStatus();
    	status.setGameUID("gameUID");
    	status.setPlayerID("playerUID");
    	status.setStatus("ready");
        return status;
    }
}
