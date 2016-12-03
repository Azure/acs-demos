package org.gardler.biglittlechallenge.trials_player;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.gardler.biglittlechallenge.trials_player.model.PlayerStatus;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		server = App.startServer();
		Client c = ClientBuilder.newClient();
		target = c.target(App.BASE_URI);
	}
	
	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Test getting the players current status.
	 */
	public void testGetPlayerStatus() {
		PlayerStatus result = target.path("player/status").request().get(PlayerStatus.class);
		assertEquals("Game status is not 'waiting'", "waiting", result.getStatus());
	}

	/**
	 * Test getting the players current status.
	 */
	public void testPutPlayerStatus() {
		PlayerStatus newStatus = new PlayerStatus();
		newStatus.setStatus("Ready");
		
		PlayerStatus result = target.path("player/status").request().buildPut(Entity.json(newStatus)).invoke(PlayerStatus.class);
		assertEquals("Game status is not 'Ready'", "Ready", result.getStatus());
	}
}
