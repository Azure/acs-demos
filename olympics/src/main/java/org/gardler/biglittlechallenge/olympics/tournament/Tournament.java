package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.tournament.event.Track100m;
import org.gardler.biglittlechallenge.olympics.tournament.event.Track8000m;

/**
 * A Tournament is a single game of Olympics. It consists of a number of events.
 * Players will enter each of the events in a tournament.
 * 
 */
public class Tournament {
	
	List<Event> events = new ArrayList<Event>();

	public Tournament(Player[] players) {
    	events.add(new Track100m(players));
    	events.add(new Track8000m(players));
	}
	
	public void start() {
		for (int i = 0; i < events.size(); i++) {
			events.get(i).playHand();
		}
	}
}
