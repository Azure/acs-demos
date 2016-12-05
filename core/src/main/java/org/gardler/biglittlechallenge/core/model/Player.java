package org.gardler.biglittlechallenge.core.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.gardler.biglittlechallenge.core.ui.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Player implements Serializable {
	private static final long serialVersionUID = -6951818858527928715L;

	private static Logger logger = LoggerFactory.getLogger(Player.class);
	
	transient AbstractUI ui;

	String name;
	Deck deck;
	
	public Player(String name, AbstractUI ui) {
		this.setName(name);
		this.setUI(ui);
	}
	
	public Deck getDeck() {
		if (deck == null) {
			createDeck(name + "'s Deck");
		}
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	
	/**
	 * Set the user interface to be used by this player.
	 * 
	 * @param ui
	 */
	public void setUI(AbstractUI ui) {
		this.ui = ui;
	}

	/**
	 * Get the user interface to be used by this player.
	 * 
	 * @param ui
	 */
	public AbstractUI getUI() {
		return this.ui;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Create a Deck for this player.
	 * 
	 * @param name the name of the deck
	 */
	public void createDeck(String name) {
		this.setDeck(this.getUI().createDeck(this));
	}
	

	/**
	 * Get the cards to be played in a specific Round
	 * @return
	 */
	public PlayedCards getCardsForHand(Round round) {
		return this.getUI().selectCards(this, round);
	}
	
	/**
	 * Save the Player object so that we can load it dynamically at
	 * application startup.
	 * @throws IOException 
	 */
	public void save() throws IOException {
          FileOutputStream fileOut = new FileOutputStream("test_1.player");
          ObjectOutputStream out = new ObjectOutputStream(fileOut);
          out.writeObject(this);
          out.close();
          fileOut.close();
          logger.info("Serialized Player in `test_1.player`");
	}
	
	public static AbstractRounds load() throws IOException, ClassNotFoundException {
		AbstractRounds hands = null;
        FileInputStream fileIn = new FileInputStream("test_1.player");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        hands = (AbstractRounds) in.readObject();
        in.close();
        fileIn.close();
        logger.info("Loaded Player definition file from `test_1.player`");
        return hands;
	}
	
	public String toString() {
		String result = this.getName();
		result = result + this.getDeck().toString();
		return result;
	}
}
