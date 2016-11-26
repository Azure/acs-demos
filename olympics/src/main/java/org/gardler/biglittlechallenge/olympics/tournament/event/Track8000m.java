package org.gardler.biglittlechallenge.olympics.tournament.event;

import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track8000m extends Event {
	
	private static Logger logger = LoggerFactory.getLogger(Track8000m.class);
	
	/**
	 * Create a new tournament involving the players provided.
	 * @param players
	 */
	public Track8000m(List<Player> players) {
		super("8000m track", players);
	}

	@Override
	protected Integer calculateRating(Character character) {
		return character.getStamina() + (character.getSpeed() / 4) + character.getDexterity();
	}
}
