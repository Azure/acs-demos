package org.gardler.biglittlechallenge.olympics.tournament;

import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sprint100m {
	
	Player[] players;
	
	private static Logger logger = LoggerFactory.getLogger(Sprint100m.class);
	
	/**
	 * Create a new tournament involving the players provided.
	 * @param players
	 */
	public Sprint100m(Player[] players) {
		this.players = players;
	}
	
    /**
     * Play the 100M sprint with the supplied players.
     * 
     * @param players
     */
	public void playHand() {
		List <Character> characters = new ArrayList<Character>();
		List <Integer> ratings = new ArrayList<Integer>();
		
		int numOfPlayers = players.length;
		for (int i = 0; i < numOfPlayers; i++) {
			Character character = players[i].playCharacter();
			characters.add(character);
			Integer rating = character.getSpeed() + character.getReactions();
			ratings.add(rating);
		}
		    	
    	Player winner = null;
    	Integer highestRating = 0;
    	for (int i = 0; i < numOfPlayers; i++ ) {
    		if (highestRating < ratings.get(i)) {
    			highestRating = ratings.get(i);
    			winner = players[i];
    		}
    	}

    	if (winner != null) {
    		logger.info("Winner of 100m sprint is " + winner.getName());
    	} else {
    		logger.info("100m sprint was a draw");
    	}
	}
}
