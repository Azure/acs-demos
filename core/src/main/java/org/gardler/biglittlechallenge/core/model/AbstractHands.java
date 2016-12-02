package org.gardler.biglittlechallenge.core.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of hands that need to be played in a game.
 *
 */
public abstract class AbstractHands implements Serializable {
	private static final long serialVersionUID = -7488746681061877408L;
	private static Logger logger = LoggerFactory.getLogger(AbstractHands.class);
	protected ArrayList<Hand> hands = new ArrayList<Hand>();

	public void add(Hand hand) {
		hands.add(hand);
	}

	public ArrayList<Hand> getAsList() {
		return hands;
	}

	public int size() {
		return hands.size();
	}

	/**
	 * Save the AbstractHands object so that we can load it dynamically at
	 * application startup.
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
	
	public static AbstractHands load() throws IOException, ClassNotFoundException {
		AbstractHands hands = null;
        FileInputStream fileIn = new FileInputStream("test.hands");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        hands = (AbstractHands) in.readObject();
        in.close();
        fileIn.close();
        logger.info("Loaded hands definition file from `test.hands`");
        return hands;
	}
}
