package org.gardler.biglittlechallenge.olympics;

import org.gardler.biglittlechallenge.olympics.ai.DumbPlayer;

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

    	System.out.println( "Hello from " + player1.getName() );
        System.out.println( player1.getDeck().getName());
        System.out.println(player1.getDeck().toString());
        
        System.out.println();
        
        System.out.println( "Hello from " + player2.getName() );
        System.out.println( player2.getDeck().getName());
        System.out.println(player2.getDeck().toString());
    }
}
