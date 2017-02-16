package org.gardler.biglittlechallenge.trials.engine;

import java.util.LinkedHashMap;

import org.gardler.biglittlechallenge.core.model.Round;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Event extends Round {

	public static Logger logger = LoggerFactory.getLogger(Event.class);

	/**
	 * Create a new Event.
	 * 
	 * @param name of the event
	 * @param formula for calculating the ratings of characters in this event (see getRatingForumla)
	 * @param players the players that will participate in this event
	 */
	public Event(String name, LinkedHashMap<String, Double> formula) {
		super(name);
		this.ratingFormula = formula;
	}
}
