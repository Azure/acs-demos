package org.gardler.biglittlechallenge.core.model;

public class Card {
	
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Card(String name) {
		this.setName(name);
	}
	
	public String toString() {
		return getName();
	}

}
