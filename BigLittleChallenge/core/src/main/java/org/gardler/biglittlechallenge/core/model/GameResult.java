package org.gardler.biglittlechallenge.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.gardler.biglittlechallenge.core.api.model.GameTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameResults captures the results of a complete game, including all rounds
 * within it.
 *
 */
public class GameResult implements Serializable {
	private static final long serialVersionUID = -1494275825119348678L;
	private static Logger logger = LoggerFactory.getLogger(GameResult.class);
	ArrayList<Round> rounds = new ArrayList<Round>();
	ArrayList<PlayerResults> players;

	public GameResult() {
		super();
		this.players = new ArrayList<PlayerResults>();
	}

	public void addRound(Round round) {
		rounds.add(round);
		ArrayList<RoundPosition> positions = round.getResults().getPositions();
		Iterator<RoundPosition> itr = positions.iterator();
		int idx = 1;
		while (itr.hasNext()) {
			RoundPosition position = itr.next();
			
			int newPoints = 0;
			//calculate points for this position
			switch (idx) {
			case 1:
				newPoints = 10;
				break;
			case 2:
				newPoints = 7;
				break;
			case 3:
				newPoints = 5;
				break;
			case 4:
				newPoints = 4;
				break;
			case 5:
				newPoints = 3;
				break;
			case 6:
				newPoints = 2;
				break;
			case 7:
				newPoints = 1;
				break;
			default:
				newPoints = 0;
			}

			GameTicket ticket = position.getTicket();
			// add new points to existing points, or create a new record
			boolean isProcessed = false;
			Iterator<PlayerResults> playerItr = players.iterator();
			while (playerItr.hasNext() && !isProcessed) {
				PlayerResults playerResult = playerItr.next();
				if (playerResult.getTicket() == ticket) {
					players.remove(playerResult);
					int points = playerResult.getPoints() + newPoints;
					playerResult.setPoints(points);
					players.add(playerResult);
					isProcessed = true;
				}
			}
			if (!isProcessed) {
				PlayerResults result = new PlayerResults();
				result.setPoints(newPoints);
				result.setTicket(ticket);
				players.add(result);
			}
			idx = idx + 1;
		}
		
		players.sort(new PlayerResultsComparator());
	}

	public ArrayList<Round> getRounds() {
		return this.rounds;
	}

	/**
	 * Get results for players that participated in this game along with their
	 * cores.
	 * 
	 * @return HashMap in which the key is the player and the value is their
	 *         score
	 */
	public ArrayList<PlayerResults> getPlayers() {
		return players;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Results table: ");
		sb.append("\n");

		// List players
		Iterator<PlayerResults> playersItr = players.iterator();
		int idx = 1;
		while (playersItr.hasNext()) {
			PlayerResults results = playersItr.next();
			sb.append("\t");
			sb.append(idx);
			sb.append(": ");
			sb.append(results.getTicket().getPlayerName());
			sb.append(" with ");
			sb.append(results.getPoints());
			sb.append(" points.\n");
			idx = idx + 1;
		}
		sb.append("\n");

		// Add individual round results
		Iterator<Round> itr = rounds.iterator();
		while (itr.hasNext()) {
			Round round = itr.next();
			sb.append(round.toString());
			sb.append("\n");
		}

		return sb.toString();
	}
	
	class PlayerResultsComparator implements Comparator<PlayerResults> {
		public int compare(PlayerResults p1, PlayerResults p2) {
			if (p1.getPoints() < p2.getPoints()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
