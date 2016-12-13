package org.gardler.biglittlechallenge.core.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of Rounds that need to be played in a game.
 *
 */
public abstract class AbstractRounds implements Serializable {
	private static final long serialVersionUID = -7488746681061877408L;
	private static Logger logger = LoggerFactory.getLogger(AbstractRounds.class);
	protected ArrayList<Round> rounds = new ArrayList<Round>();

	public void add(Round round) {
		rounds.add(round);
	}

	public ArrayList<Round> getAsList() {
		return rounds;
	}

	public int size() {
		return rounds.size();
	}

	/**
	 * Save the AbstractHands object so that we can load it dynamically at
	 * application startup.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("test.hands");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(this);
		out.close();
		fileOut.close();
		logger.info("Serialized Hands in `test.hands`");
	}

	/**
	 * Load the rounds for a tournament from disk. If no saved file is foound
	 * return null, otherwise return the loaded rounds.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static AbstractRounds load() throws ClassNotFoundException, IOException {
		AbstractRounds hands = null;
		try {
			FileInputStream fileIn;
			fileIn = new FileInputStream("test.hands");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			hands = (AbstractRounds) in.readObject();
			in.close();
			fileIn.close();
			logger.info("Loaded hands definition file from `test.hands`");
		} catch (FileNotFoundException e) {
			return null;
		}
		return hands;
	}
}
