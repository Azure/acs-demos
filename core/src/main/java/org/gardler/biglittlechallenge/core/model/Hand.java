package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;

/**
 * A hand represents a single hand in a game.
 *
 */
public abstract class Hand implements Serializable {
	private static final long serialVersionUID = 4507943821303911470L;
	protected String name;
	
	public Hand(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return getName();
	}

}
