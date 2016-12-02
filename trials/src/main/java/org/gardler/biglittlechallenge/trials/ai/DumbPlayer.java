package org.gardler.biglittlechallenge.trials.ai;

import org.gardler.biglittlechallenge.trials.model.Player;

/**
 * DumbPlayer is a simple AI, a very simple AI. It always plays a random card regardless of the circumstances.
 * 
 */
public class DumbPlayer extends Player {
	public DumbPlayer(String name) {
		super(name, new DumbAIUI());
	}

}
