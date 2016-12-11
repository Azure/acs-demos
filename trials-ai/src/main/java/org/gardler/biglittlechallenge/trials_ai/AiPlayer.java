package org.gardler.biglittlechallenge.trials_ai;

import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.gardler.biglittlechallenge.trials.model.Player;

public class AiPlayer extends Player {
	private static final long serialVersionUID = -541305944014913776L;
	static Player instance;
	
	public AiPlayer(String name) {
		super(name, new DumbAIUI());
		instance = this;
	}
	
	public static Player getInstance() {
		if (instance == null) {
			instance = new AiPlayer("Default AI Player");
		}
		return instance;
	}

}
