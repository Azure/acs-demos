package org.gardler.biglittlechallenge.trials.ai.player;

import org.gardler.biglittlechallenge.core.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiPlayer extends Player {
	private static final long serialVersionUID = -541305944014913776L;
	private static Logger logger = LoggerFactory.getLogger(AiPlayer.class);
	Player instance;

	public AiPlayer(String name) {
		super(name, "org.gardler.biglittlechallenge.trials.ai.player.DumbAIUI");
		instance = this;
	}

}
