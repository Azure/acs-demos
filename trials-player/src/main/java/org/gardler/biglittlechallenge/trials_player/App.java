package org.gardler.biglittlechallenge.trials_player;

import org.gardler.biglittlechallenge.trials.model.Player;
import org.gardler.biglittlechallenge.trials_player.ui.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Hello world!
 *
 */
public class App 
{
	private static Logger logger = LoggerFactory.getLogger(App.class);
	Player player;
	// Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/v0.1/";
    
	public App() {
		player = new Player("Jane", new Shell());
	}
	
    public static void main( String[] args )
    {
		App.startServer();
        logger.info(String.format("Client API started with WADL available at "
                + "%sapplication.wadl", BASE_URI));
        while (true) {
        	
        }
    }
    

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().register(API.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
}
