package org.gardler.biglittlechallenge.olympics.ui;

import java.io.IOException;

import org.gardler.biglittlechallenge.olympics.tournament.Tournament;

public class Shell {

	private Tournament tournament;

	public Shell(Tournament tournament) {
		this.tournament = tournament;
	}

	public void run() throws IOException {
		print("Welcome to the Olympics");
		while (true) {
			displayMainMenu();
			int choice = getChoice(" Select option: ");
			switch (choice) {
			case 1:
				tournament.start();
				break;
			case 2: 
				print("Thanks for playing.");
				System.exit(1);
			default:
				print("Invalid selection");
			}
		}
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
		print("1. Start Tournament");
		print("2. Quit game");
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
		int dummy;
		int bAvail;

		try {
			while ((System.in.available()) != 0)
				dummy = System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input error");
		}
	}
}
