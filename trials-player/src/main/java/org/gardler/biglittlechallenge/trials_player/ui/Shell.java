package org.gardler.biglittlechallenge.trials_player.ui;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.Hand;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell extends AbstractUI {
	
	private static Logger logger = LoggerFactory.getLogger(Shell.class);

	public Shell() {
		super();
		logger.info("Created Player UI");
	}
	
	@Override
	public Card selectCard(Player player, Hand hand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Deck createDeck(Player player) {
		// TODO Auto-generated method stub
		return null;
	}
}