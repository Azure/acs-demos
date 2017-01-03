package org.gardler.biglittlechallenge.core.api.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents the current results for a player in an active game.
 *
 */
public class PlayerResultsResponse implements Serializable {
	private static final long serialVersionUID = 7081766884917303112L;
	
	private UUID id;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	private int points;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	
}
