package org.gardler.biglittlechallenge.trials.model;

import java.util.Random;

public class Character extends org.gardler.biglittlechallenge.core.model.Card {
	private static final long serialVersionUID = 1873696867527064976L;
	private static int NUM_OF_TRAITS = 7;
	private static int POINTS_PER_TRAIT = 10;

	/**
	 * Create a new card with a given name. The statistics for this card will be generated
	 * randomly by distributing a fixed amount of points across all attributes.
	 * 
	 * @param name
	 */
	public Character(String name) {
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

		points = getRandomPoints(allowedPoints - assignedPoints);
		setSpeed(points);
		assignedPoints = assignedPoints + points - 1;

		setReactions(allowedPoints - assignedPoints);
	}
	
	private void setSrength(int points) {
		setProperty("Strength", Integer.toString(points));
	}
	
	private void setDexterity(int points) {
		setProperty("Dexterity", Integer.toString(points));
	}
	public int getDexterity() {
		return Integer.parseInt(getProperty("Dexterity"));
	}
	
	public void setReactions(int points) {
		setProperty("Reactions", Integer.toString(points));
	}
	public int getReactions() {
		return Integer.parseInt(getProperty("Reactions"));
	}
	
	public void setSpeed(int points) {
		setProperty("Speed", Integer.toString(points));
	}
	public int getSpeed() {
		return Integer.parseInt(getProperty("Speed"));
	}
	
	private void setIntelligence(int points) {
		setProperty("Intelligence", Integer.toString(points));
	}
	
	private void setCharisma(int points) {
		setProperty("Charisma", Integer.toString(points));
	}
	
	public void setStamina(int points) {
		setProperty("Stamina", Integer.toString(points));
	}
	public int getStamina() {
		return Integer.parseInt(getProperty("Stamina"));
	}
	
	private int getRandomPoints(int allowedPoints) {
		Random rand = new Random();
		if (allowedPoints > POINTS_PER_TRAIT * 2) {
			allowedPoints = POINTS_PER_TRAIT * 2;
		}
		return rand.nextInt(allowedPoints) + 1;
	}
					
}
