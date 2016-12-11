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
		writeProperty("Strength", Integer.toString(points));
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		writeProperty("Dexterity", Integer.toString(points));
		assignedPoints = assignedPoints + points -1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		writeProperty("Intelligence", Integer.toString(points));
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		writeProperty("Charisma", Integer.toString(points));
		assignedPoints = assignedPoints + points - 1;
		
		points = getRandomPoints(allowedPoints - assignedPoints);
		writeProperty("Stamina", Integer.toString(points));
		assignedPoints = assignedPoints + points - 1;

		points = getRandomPoints(allowedPoints - assignedPoints);
		writeProperty("Speed", Integer.toString(points));
		assignedPoints = assignedPoints + points - 1;

		writeProperty("Reactions", Integer.toString(allowedPoints - assignedPoints));
	}
	
	
	private int getRandomPoints(int allowedPoints) {
		Random rand = new Random();
		if (allowedPoints > POINTS_PER_TRAIT * 2) {
			allowedPoints = POINTS_PER_TRAIT * 2;
		}
		return rand.nextInt(allowedPoints) + 1;
	}
					
}
