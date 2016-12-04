package org.gardler.biglittlechallenge.trials_player.ui;

import org.gardler.biglittlechallenge.trials.ai.DumbAIUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell extends DumbAIUI {
	
	private static Logger logger = LoggerFactory.getLogger(Shell.class);

	public Shell() {
		super();
		logger.info("Created Player UI");
	}
}