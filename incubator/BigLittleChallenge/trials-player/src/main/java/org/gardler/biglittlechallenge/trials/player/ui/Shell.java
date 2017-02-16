package org.gardler.biglittlechallenge.trials.player.ui;

import java.io.IOException;
import java.util.Iterator;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.PlayedCards;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.model.PlayerStatus;
import org.gardler.biglittlechallenge.core.model.Round;
import org.gardler.biglittlechallenge.trials.model.Character;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell extends AbstractUI {
	private static final long serialVersionUID = 501714466944966557L;
	private static Logger logger = LoggerFactory.getLogger(Shell.class);

	public Shell() {
		super();
		logger.info("Created Player UI");
	}
	
	protected void print(String msg) {
		System.out.println(msg);
		System.out.flush();
	}
	
	/**
	 * Ensure the input stream is empty.
	 */
	protected static void inputFlush() {
		@SuppressWarnings("unused")
		int dummy;
		@SuppressWarnings("unused")
		int bAvail;

		try {
			while ((System.in.available()) != 0)
				dummy = System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input error");
		}
	}
	
	/**
	 * Get user input as a string.
	 * @return
	 */
	protected static String inString() {
		int aChar;
		String result = "";
		boolean finished = false;

		while (!finished) {
			try {
				aChar = System.in.read();
				if (aChar < 0 || (char) aChar == '\n')
					finished = true;
				else if ((char) aChar != '\r')
					result = result + (char) aChar; // Enter into string
			} catch (java.io.IOException e) {
				System.out.println("Input error");
				finished = true;
			}
		}
		return result;
	}
	

	/**
	 * Get user input as an integer.
	 * @return
	 */
	protected int inInt(String msg) {
		while (true) {
			inputFlush();
			print(msg);
			try {
				return Integer.valueOf(inString().trim()).intValue();
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Not an integer");
			}
		}
	}

	@Override
	public PlayedCards selectCards(Player player, Round round) {
		PlayedCards playedCards = new PlayedCards();
		print("\n" + player.getName() + " Your hand currently contains the following cards:\n");

		Deck deck = (Deck) player.getDeck();
		Iterator<Card> cards = player.getDeck().getCards().values().iterator();
		while (cards.hasNext()) {
			Card card = cards.next();
			print(card.toString());
		}

		while (true) {
			print("Enter the name of the card you wish to play for " + round.getName());
			String key = inString();
			if (deck.getCards().containsKey(key)) {
				playedCards.addCard(deck.getCard(key));
				return playedCards;
			} else {
				print("That's not a valid card name.");
			}
		}
	}

	@Override
	public Deck createDeck(Player player) {
		logger.info("Creating deck for " + player.getName());
		Deck deck = new Deck(player.getName());
		int numOfCards = 4;
		
		while (numOfCards > deck.getCards().size()) {
			print(player.getName() + " enter the name of a card (you need to create " + (numOfCards - deck.getCards().size()) + " more cards)");
			String name = inString();
			Character card = new Character(name);

			boolean isCardDone = false;
			int spareValue = 0;
			while (! isCardDone) {
				print(card.toString());
				print(player.getName() + " enter the name of a trait you would like to change (or 'done' once completed)");
				String trait = inString();
				if (trait.toLowerCase().equals("done")) {
					if (spareValue == 0) {
						isCardDone = true;
					} else if (spareValue > 0) {
						print("You are not done, you still have " + spareValue + " points to allocate.");
					} else {
						print("You are not done, you have spent " + Math.abs(spareValue) + " too many points.");
					}
				} else {
					try {
						Integer value = card.getPropertyAsInteger(trait);
						int desiredValue = inInt("What value do you want " + trait + " to be?");
						spareValue = spareValue + (value - desiredValue);
						card.writeProperty(trait, Integer.toString(desiredValue));
						if (spareValue == 0) {
							print("You have spent all your points");
						} else if (spareValue < 0) {
							print("You have overspent by " + Math.abs(spareValue) + " please reduce one or more traits.");
						} else {
							print("You have underspent by " + spareValue + " please increase one or more traits.");
						}
					} catch (IllegalArgumentException e) {
						print("That is not a valid input, please try again");
					}
				}
			}
			deck.addCard(card);
		}
    	
		player.setDeck(deck);
		try {
			player.save();
		} catch (IOException e) {
			logger.info("Unable to save player", e);
		}
    	return deck;
	}

	@Override
	public PlayerStatus startGame(Player player) {
		PlayerStatus state = player.getStatus();
		state.setState(PlayerStatus.State.Playing);
		player.setStatus(state);
		return state;
	}
}