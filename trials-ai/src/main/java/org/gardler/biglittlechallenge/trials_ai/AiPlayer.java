package org.gardler.biglittlechallenge.trials_ai;

import org.gardler.biglittlechallenge.trials.model.Player;

public class AiPlayer {
	
	static Player instance;
	
	public AiPlayer(String name) {
		if (instance == null) {
			instance = new Player(name, null);
		}
	}

}
