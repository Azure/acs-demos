package org.gardler.biglittlechallenge.olympics.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.core.model.Deck;
import org.gardler.biglittlechallenge.core.model.Hand;
import org.gardler.biglittlechallenge.core.model.Player;
import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.tournament.Event;
import org.gardler.biglittlechallenge.olympics.tournament.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell extends AbstractUI {
	
	private static Logger logger = LoggerFactory.getLogger(Shell.class);

	private Tournament tournament;
	private List<Player> players;

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Shell() {
		super();
	}
	
	public void run() throws IOException {
		print("Welcome to the Olympics");
		while (true) {
			displayMainMenu();
			int choice = inInt("\nSelect option: ");
			switch (choice) {
			case 1:
				print(tournament.toString());
				break;
			case 2:
				runTournament();
				break;
			case 9: 
				quit();
			default:
				print("Invalid selection");
			}
		}
	}

	private void runTournament() {
		int choice;
		Iterator<Hand> events = tournament.getHands().iterator();
		while (events.hasNext()) {
			Event event = (Event)events.next();
			displayEventMenu(event);
			choice = inInt("\nSelect option: ");
			switch (choice) {
			case 1:
				play(event);
				break;
			case 9:
				quit();
			default:
				print("Invalid selection");
			}
		}
	}

	private void quit() {
		print("Thanks for playing.");
		System.exit(1);
	}

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

	protected void print(String msg) {
		System.out.println(msg);
		System.out.flush();
	}

	protected void displayMainMenu() {
		print("\nOptions");
		print("=======");
		print("\t1. Describe Tournament");
		print("\t2. Start Tournament");
		print("\t9. Quit game");
	}
	
	protected void displayEventMenu(Event event) {
		print("\nOptions");
		print("=======");
		print("\t1. Play hand - " + event.getName());
		print("\t9. Quit game");
	}

	public static String inString() {
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

	// Ensure the input stream is empty
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

	public void play(Event event) {
		event.playHand(players);
	}

	@Override
	public Card selectCard(org.gardler.biglittlechallenge.core.model.Player player, Hand hand) {
		print("\n" + player.getName() + " Your hand currently contains the following cards:\n");
		
		Deck deck = (Deck) player.getDeck();
		
		Iterator<Card> cards = player.getDeck().getCards().values().iterator();
		while (cards.hasNext()) {
			Card card = cards.next();
			print(card.toString());
		}
		
		while (true) {
			print("Current hand is " + hand.toString());
			print("Enter the name of the card you wish to play for this hand:");
			String key = inString();
			
			if (deck.getCards().containsKey(key)) {	
				return deck.getCard(key);
			} else {
				print("That's not a valid card name.");
			}
		}
	}
	
	@Override
	public Deck createDeck(Player player) {
		logger.info("Creating deck for " + player.getName());
		Deck deck = new Deck(player.getName());
		int numOfHands = this.tournament.getHands().size();
		
		while (numOfHands > deck.getCards().size()) {
			print(player.getName() + " enter the name of a card (you need to create " + (numOfHands - deck.getCards().size()) + " more cards)");
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
						card.setProperty(trait, Integer.toString(desiredValue));
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
    	return deck;
	}
}
