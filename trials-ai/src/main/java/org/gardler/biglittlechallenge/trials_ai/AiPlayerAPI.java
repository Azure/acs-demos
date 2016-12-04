package org.gardler.biglittlechallenge.trials_ai;

import javax.ws.rs.Path;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.gardler.biglittlechallenge.trials.api.AbstractPlayerAPI;

/**
 * Player API is used to communicate with the player.
 *
 */
@Path("player")
public class AiPlayerAPI extends AbstractPlayerAPI {
	
	public AiPlayerAPI(String name, AbstractUI ui) {
		super(name, ui);
	}

	
}
