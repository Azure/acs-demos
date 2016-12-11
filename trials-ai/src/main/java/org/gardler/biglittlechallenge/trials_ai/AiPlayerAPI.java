package org.gardler.biglittlechallenge.trials_ai;

import javax.ws.rs.Path;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.api.AbstractPlayerAPI;
import org.gardler.biglittlechallenge.trials.model.Player;

/**
 * Player API is used to communicate with the player.
 *
 */
@Path("player")
public class AiPlayerAPI extends AbstractPlayerAPI {

	public AiPlayerAPI(String name, AbstractUI ui) {
		super(new org.gardler.biglittlechallenge.trials.model.Player(name, ui), ui);
	}

	public AiPlayerAPI(Player player, DumbAIUI ui) {
		super(player, ui);
	}

}
