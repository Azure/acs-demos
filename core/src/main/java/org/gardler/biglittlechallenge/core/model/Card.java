package org.gardler.biglittlechallenge.core.model;

import java.util.HashMap;
import java.util.Iterator;

public class Card {
	
	String name;
	HashMap<String, String> properties = new HashMap<String, String>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Card(String name) {
		this.setName(name);
	}
	
	public String toString() {
		String result = getName() + "\n";
		Iterator<String> itr = properties.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = properties.get(key);
			result = result + "\t" + key + " = " + value + "\n";
		}
		return result;
	}
	
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	public String getProperty(String key) {
		return properties.get(key);
	}

	public Integer getPropertyAsInteger(String key) {
		if (properties.containsKey(key)) {
			return Integer.parseInt(properties.get(key));
		} else {
			throw new IllegalArgumentException("Card property '" + key + "' does not exist.");
		}
	}

}
