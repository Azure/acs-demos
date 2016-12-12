package org.gardler.biglittlechallenge.trials.ai;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gardler.biglittlechallenge.trials.model.Player;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiPlayer extends Player {
	private static final long serialVersionUID = -541305944014913776L;
	private static Logger logger = LoggerFactory.getLogger(AiPlayer.class);
	static Player instance;
	private static String engineEndpoint = null;

	public AiPlayer(String name) {
		super(name, new DumbAIUI());
		instance = this;
	}

	public static Player getInstance() {
		if (instance == null) {
			instance = new AiPlayer("Default AI Player");
		}
		return instance;
	}

	/**
	 * Return the endpoint of an available engine API.
	 * 
	 * @return
	 * @throws EngineNotFoundException
	 */
	public static String getEngineEndoint() {
		int index = 0;
		String[] url = { "http://localhost:8080/", "http://engine:8080/" };
		
		while (engineEndpoint == null) { 
			Response response = checkEngineStatus(url[index]);
			if (response != null) {
				if (response.getStatus() != 200) {
					logger.warn("Got a response other than 200 from Engine at " + url + " - " + response.getStatus());
				} else {
					engineEndpoint = url[index];
				}
			}
			
			index = index + 1;
			if (index > url.length) {
				index = 0;
				logger.warn("Unable to connect to an available engine, waiting then trying again");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.warn("Got interupted while sleeping");
				}
			}
		}
		
		logger.debug("Connected to engine at " + engineEndpoint);
		return engineEndpoint;
	}

	/**
	 * Attempts to connect to an Engine API. Returns null if no response or the
	 * response object containing the response to a GET of the current status.
	 * 
	 * @param statusPath
	 * @param url
	 * @return
	 */
	private static Response checkEngineStatus(String url) {
		String statusPath = "api/v0.1/tournament/status";
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget webTarget = client.target(url).path(statusPath);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		try {
			Response response = invocationBuilder.get();
			logger.debug("Engine response recieved from " + url + statusPath + " : " + response.readEntity(String.class));
			return response;
		} catch (Exception e) {
			logger.debug("Unable to get engine status from " + url + statusPath + " - " + e.getMessage());
			return null;
		}
	}

}
