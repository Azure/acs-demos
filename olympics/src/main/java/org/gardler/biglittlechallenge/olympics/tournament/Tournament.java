package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Player;

/**
 * A Tournament is a single game of Olympics. It consists of a number of events.
 * Players will enter each of the events in a tournament.
 * 
 */
public class Tournament {
	
	List<Event> events = new ArrayList<Event>();
	List<Player> players;
	
	public Tournament(List<Player> players) {
		this.players = players;
		
		LinkedHashMap<String, Double> formula = new LinkedHashMap<String, Double>();
		formula.put("Speed", 1.0);
		formula.put("Reactions", 0.5);
		Event event = new Event("Track: 100m Sprint", formula);
    	events.add(event);
    	
		formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		event = new Event("Track: 8000m", formula);
    	events.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
		formula.put("Stamina", 1.0);
		formula.put("Speed", 0.25);
		formula.put("Dexterity", 1.0);
		event = new Event("Track: 8000m Steeple Chase", formula);
    	events.add(event);
    	
    	formula = new LinkedHashMap<String, Double>();
    	formula.put("Dexterity", 1.0);
		formula.put("Strength", 0.5);
		event = new Event("Field: Pole Vault", formula);
    	events.add(event);
	}
	
	public List<Event> getEvents() {
		return this.events;
	}
	
	public String toString() {
		String result = "This tournament consists of " + events.size() + " events.\n";
		Iterator<Event> itr = events.iterator();
		while (itr.hasNext()) {
			Event event = itr.next();
			result = result + "\t" + event.getName() + "\n";
		}
		return result;
	}
}
