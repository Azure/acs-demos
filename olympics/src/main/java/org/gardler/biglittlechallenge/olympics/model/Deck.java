package org.gardler.biglittlechallenge.olympics.model;

public class Deck extends org.gardler.biglittlechallenge.core.model.Deck {

	public Deck(String name) {
		super(name);
	}

	public Character getCharacter(String key) {
		return (Character)getCards().get(key);
	}

}
