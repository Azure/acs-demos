package org.gardler.biglittlechallenge.core.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Represents the result of one round in a game.
 *
 */
public class RoundResult {
	ArrayList<Position> positions = new ArrayList<Position>();

	public RoundResult() {
		super();
	}

	public void addResult(Player player, PlayedCards cards, Double rating) {
		Position position = new Position(player, cards, rating);
		positions.add(position);
		positions.sort(new PositionComparator());
	}

	/**
	 * Get a sorted ArrayList of all players in their positions for a round.
	 * First in the array is the winner.
	 * 
	 * @return
	 */
	public ArrayList<Position> getPositions() {
		return this.positions;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		int idx = 1;
		Iterator<Position> itr = positions.iterator();
		while (itr.hasNext()) {
			Position position = itr.next();
			result.append(idx);
			result.append(": ");
			result.append(position.toString());
			result.append("\n");
			idx = idx + 1;
		}
		return result.toString();
	}

	/**
	 * Represents the information about the player and the cards played a given
	 * position.
	 *
	 */
	class Position {
		Player player;
		PlayedCards cards;
		Double rating;

		public Position(Player player, PlayedCards cards, Double rating) {
			this.player = player;
			this.cards = cards;
			this.rating = rating;
		}

		public Double getRating() {
			return this.rating;
		}
		
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(player.getName());
			result.append(" (");
			result.append(getRating());
			result.append(") with ");
			result.append(cards.toString());
			result.append("\n");
			return result.toString();
		}
	}

	class PositionComparator implements Comparator<Position> {
		public int compare(Position p1, Position p2) {
			if (p1.getRating() < p2.getRating()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
