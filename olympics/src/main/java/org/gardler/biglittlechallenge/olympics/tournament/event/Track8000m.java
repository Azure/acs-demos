package org.gardler.biglittlechallenge.olympics.tournament.event;

import java.util.LinkedHashMap;
import java.util.List;

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
	protected LinkedHashMap<String, Double> getRatingFormula() {
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		formula.put("Dexterity", 1.0);
		return formula;
	}
}
