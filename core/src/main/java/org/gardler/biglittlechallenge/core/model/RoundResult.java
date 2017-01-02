package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Represents the result of one round in a game.
 *
 */
public class RoundResult implements Serializable {
	private static final long serialVersionUID = 4741823086901718121L;
	ArrayList<RoundPosition> positions = new ArrayList<RoundPosition>();

	public RoundResult() {
		super();
	}

	public void addResult(Player player, PlayedCards cards, Double rating) {
		RoundPosition position = new RoundPosition(player, cards, rating);
		positions.add(position);
		positions.sort(new PositionComparator());
	}

	/**
	 * Get a sorted ArrayList of all players in their positions for a round.
	 * First in the array is the winner.
	 * 
	 * @return
	 */
	public ArrayList<RoundPosition> getPositions() {
		return this.positions;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		int idx = 1;
		Iterator<RoundPosition> itr = positions.iterator();
		while (itr.hasNext()) {
			RoundPosition position = itr.next();
			result.append(idx);
			result.append(": ");
			result.append(position.toString());
			result.append("\n");
			idx = idx + 1;
		}
		return result.toString();
	}

	class PositionComparator implements Comparator<RoundPosition> {
		public int compare(RoundPosition p1, RoundPosition p2) {
			if (p1.getRating() < p2.getRating()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
