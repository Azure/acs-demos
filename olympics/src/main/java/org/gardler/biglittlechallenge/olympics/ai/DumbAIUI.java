package org.gardler.biglittlechallenge.olympics.ai;

import java.util.Random;
import java.util.Set;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.core.model.Hand;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.gardler.biglittlechallenge.olympics.model.Deck;

public class DumbAIUI extends AbstractUI {

	@Override
	public Card selectCard(Player player, Hand hand) {
		Deck deck = (Deck) player.getDeck();
		Set<String> keys = player.getDeck().getCards().keySet();

		Random rand = new Random();
		int idx = rand.nextInt(keys.size());
		String key = (String) keys.toArray()[idx];
		return  deck.getCharacter(key);
	}
}
