package org.gardler.biglittlechallenge.trials.ai;

import javax.ws.rs.Path;

import org.gardler.biglittlechallenge.trials.api.AbstractPlayerAPI;
import org.gardler.biglittlechallenge.trials.model.Player;

/**
 * Player API is used to communicate with the player.
 *
 */
@Path("player")
public class AiPlayerAPI extends AbstractPlayerAPI {

	public AiPlayerAPI(String name, String uiClassName) {
		super(new org.gardler.biglittlechallenge.trials.model.Player(name, uiClassName));
	}

	public AiPlayerAPI(Player player) {
		super(player);
	}

}
