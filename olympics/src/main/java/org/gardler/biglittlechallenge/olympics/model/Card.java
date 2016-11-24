package org.gardler.biglittlechallenge.olympics.model;

import java.util.Random;

public class Card extends org.gardler.biglittlechallenge.core.model.Card {
	
	private static int NUM_OF_TRAITS = 6;
	private static int POINTS_PER_TRAIT = 10;

	/**
	 * Create a new card with a given name. The statistics for this card will be generated
	 * randomly by distributing a fixed amount of points across all attributes.
	 * 
	 * @param name
	 */
	public Card(String name) {
		super(name);
		int assignedPoints = NUM_OF_TRAITS;
		int allowedPoints = POINTS_PER_TRAIT * NUM_OF_TRAITS;
		
		int points = getRandomPoints(allowedPoints - assignedPoints);
		setSrength(points);
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		setDexterity(points);
		assignedPoints = assignedPoints + points -1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		setIntelligence(points);
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		setCharisma(points);
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		setStamina(points);
		assignedPoints = assignedPoints + points - 1;
		
		setReactions(allowedPoints - assignedPoints);
	}
	
	private void setSrength(int points) {
		setProperty("Strength", Integer.toString(points));
	}
	
	private void setDexterity(int points) {
		setProperty("Dexterity", Integer.toString(points));
	}
	
	private void setReactions(int points) {
		setProperty("Reactions", Integer.toString(points));
	}
	
	private void setIntelligence(int points) {
		setProperty("Intelligence", Integer.toString(points));
	}
	
	private void setCharisma(int points) {
		setProperty("Charisma", Integer.toString(points));
	}
	
	private void setStamina(int points) {
		setProperty("Stamina", Integer.toString(points));
	}
		
	private int getRandomPoints(int allowedPoints) {
		Random rand = new Random();
		return rand.nextInt((allowedPoints - 1) + 1) + 1;
	}
	
	public int strength;
	public int dexterity;
	public int intelligence;
	public int charisma;
	public int reactions;
	public int stamina;
					
}
