package org.gardler.biglittlechallenge.olympics.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.Event;
import org.gardler.biglittlechallenge.olympics.tournament.Tournament;

public class Shell {

	private Tournament tournament;
	private List<Player> players;

	public Shell(Tournament tournament, List<Player> players) {
		this.tournament = tournament;
		this.players = players;
	}

	public void run() throws IOException {
		print("Welcome to the Olympics");
		while (true) {
			displayMainMenu();
			int choice = getChoice("\nSelect option: ");
			switch (choice) {
			case 1:
				print(tournament.toString());
				break;
			case 2:
				Iterator<Event> events = tournament.getEvents().iterator();
				while (events.hasNext()) {
					Event event = events.next();
					displayEventMenu(event);
					choice = getChoice("\nSelect option: ");
					switch (choice) {
					case 1:
						event.playHand(players);
						break;
					case 9:
						quit();
					default:
						print("Invalid selection");
					}
				}
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

	protected int getChoice(String msg) {
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
}
