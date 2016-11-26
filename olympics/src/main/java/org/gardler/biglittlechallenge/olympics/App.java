package org.gardler.biglittlechallenge.olympics;

import java.util.ArrayList;
import java.util.List;

import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;
import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.model.Player;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	DumbPlayer player1 = new DumbPlayer("AI Player One");
    	DumbPlayer player2 = new DumbPlayer("AI Player Two");
    	DumbPlayer player3 = new DumbPlayer("AI Player Three");
    	DumbPlayer player4 = new DumbPlayer("AI Player Four");
    	DumbPlayer player5 = new DumbPlayer("AI Player Five");

    	Player[] players = { player1, player2, player3, player4, player5 };
    	plau100MSprint(players);
    }

    /**
     * Play the 100M sprint with the supplied players.
     * 
     * @param players
     */
	private static void plau100MSprint(Player[] players) {
		List <Character> characters = new ArrayList<Character>();
		List <Integer> ratings = new ArrayList<Integer>();
		
		int numOfPlayers = players.length;
		for (int i = 0; i < numOfPlayers; i++) {
			System.out.println(players[i]);
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
    		System.out.println("Winner of 100m sprint is " + winner.getName());
    	} else {
    		System.out.println("100m sprint was a draw");
    	}
	}
}
