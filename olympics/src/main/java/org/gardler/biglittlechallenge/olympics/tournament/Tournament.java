package org.gardler.biglittlechallenge.olympics.tournament;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.gardler.biglittlechallenge.core.model.AbstractGame;
import org.gardler.biglittlechallenge.core.model.AbstractHands;
import org.gardler.biglittlechallenge.core.model.Hand;
import org.gardler.biglittlechallenge.core.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Tournament is a single game of Olympics. It consists of a number of events.
 * Players will enter each of the events in a tournament.
 * 
 */
public class Tournament extends AbstractGame {
	
	private static Logger logger = LoggerFactory.getLogger(Tournament.class);
			
	public Tournament(List<Player> players) {
		super(players);
	}
	
	protected void setHands() {
		try {
			hands = AbstractHands.load();
			return;
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		} catch (IOException e) {
			logger.error("Unable to load hands definition, using default hands.", e);
		}
		hands = new Hands();
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Speed", 1.0);
		formula.put("Reactions", 0.5);
		Event event = new Event("Track: 100m Sprint", formula);
    	hands.add(event);
    	
		formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		event = new Event("Track: 8000m", formula);
    	hands.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		formula.put("Dexterity", 1.0);
		event = new Event("Track: 8000m Steeple Chase", formula);
    	hands.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
    	formula.put("Dexterity", 1.0);
		formula.put("Strength", 0.5);
		event = new Event("Field: Pole Vault", formula);
    	hands.add(event);
	}
	
	public String toString() {
		String result = "This tournament consists of " + hands.size() + " events.\n";
		Iterator<Hand> itr = getHands().iterator();
		while (itr.hasNext()) {
			Event event = (Event)itr.next();
			result = result + "\t" + event.getName() + "\n";
		}
		return result;
	}
}
