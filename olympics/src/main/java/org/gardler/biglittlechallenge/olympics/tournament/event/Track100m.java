package org.gardler.biglittlechallenge.olympics.tournament.event;

import java.util.LinkedHashMap;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track100m extends Event {

	private static Logger logger = LoggerFactory.getLogger(Track100m.class);

	/**
	 * Create a new event involving the players provided.
	 * 
	 * @param players
	 */
	public Track100m(List<Player> players) {
		super("100m Sprint", players);
	}

	@Override
	protected LinkedHashMap<String, Double> getRatingFormula() {
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Speed", 1.0);
		formula.put("Reactions", 0.5);
		return formula;
	}
}
