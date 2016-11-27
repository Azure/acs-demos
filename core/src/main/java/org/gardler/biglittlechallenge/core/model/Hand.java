package org.gardler.biglittlechallenge.core.model;

/**
 * A hand represents a single hand in a game.
 *
 */
public abstract class Hand {

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
