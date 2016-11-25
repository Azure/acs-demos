package org.gardler.biglittlechallenge.olympics;

import org.gardler.biglittlechallenge.olympics.model.Deck;
import org.gardler.biglittlechallenge.olympics.model.Player;
import org.gardler.biglittlechallenge.olympics.model.Character;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Player player1 = new Player("Player One");
    	
    	Deck deck = new Deck("Player 1 First Deck");
    	Character card = new Character("Foo");
    	deck.addCard(card);
    	card = new Character("Potato");
    	deck.addCard(card);
    	player1.setDeck(deck);
    	
    	Player player2 = new Player("Player Two");
    	deck = new Deck("Player 2 First Deck");
    	card = new Character("Foo");
    	deck.addCard(card);
    	card = new Character("Pie");
    	deck.addCard(card);
    	player2.setDeck(deck);
    	
        System.out.println( "Hello from " + player1.getName() );
        System.out.println( player1.getDeck().getName());
        System.out.println(player1.getDeck().toString());
        
        System.out.println();
        
        System.out.println( "Hello from " + player2.getName() );
        System.out.println( player2.getDeck().getName());
        System.out.println(player2.getDeck().toString());
    }
}
