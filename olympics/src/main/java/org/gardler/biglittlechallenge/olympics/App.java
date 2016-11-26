package org.gardler.biglittlechallenge.olympics;

import org.gardler.biglittlechallenge.core.model.Card;
import org.gardler.biglittlechallenge.olympics.model.Character;
import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;
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

    	Character player1card = player1.playCharacter();
    	Character player2card = player2.playCharacter();
    	
    	// Play 100M Sprint
    	int player1Rating = player1card.getSpeed() + player1card.getReactions();
    	int player2Rating = player2card.getSpeed() + player2card.getReactions();
    	
    	Player winner;
    	if (player1Rating > player2Rating) {
    		winner = player1;
    	} else if (player1Rating < player2Rating) {
    		winner = player2;
    	} else {
    		winner = null;
    	}

    	System.out.println(player1.toString());
    	System.out.println("Player 1 rating for 100m Sprint = " + player1Rating);

    	System.out.println(player2.toString());
    	System.out.println("Player 2 rating for 100m Spring = " + player2Rating);
    	System.out.println("");
    	
    	if (winner != null) {
    		System.out.println("Winner of 100m sprint is " + winner.getName());
    	} else {
    		System.out.println("100m sprint was a draw");
    	}
    }
}
