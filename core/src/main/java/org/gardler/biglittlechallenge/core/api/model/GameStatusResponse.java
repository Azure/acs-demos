package org.gardler.biglittlechallenge.core.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.gardler.biglittlechallenge.core.model.GameStatus;
import org.gardler.biglittlechallenge.core.model.PlayerResults;
import org.gardler.biglittlechallenge.core.model.GameStatus.State;

/**
 * An object returned via the API whenever the Game Status is needed.
 *
 */
public class GameStatusResponse implements Serializable {
	private static final long serialVersionUID = 734742551126798205L;

	private UUID gameUUID;
	private State state;
    private ArrayList<PlayerResultsResponse> playerResults = new ArrayList<PlayerResultsResponse>();
    private String name;
    
    
    /**
     * Typically the empty constructor will only be used when deserializing.
     */
    public GameStatusResponse() {
    	super();
    }
    
    /**
     * Create a response object from a GameStatus object.
     */
    public GameStatusResponse(GameStatus status) {
    	setGameUUID(status.getGameUUID());
		setState(status.getState());
		
		Iterator<PlayerResults> itr = status.getResults().getPlayers().iterator();
		while (itr.hasNext()) {
			PlayerResults playerResult = itr.next();
			PlayerResultsResponse pResponse = new PlayerResultsResponse();
			pResponse.setId(playerResult.getPlayer().getID());
			pResponse.setName(playerResult.getPlayer().getName());
			pResponse.setPoints(playerResult.getPoints());
			playerResults.add(pResponse);
		}
    }
    
	public UUID getGameUUID() {
		return gameUUID;
	}
	public void setGameUUID(UUID gameUUID) {
		this.gameUUID = gameUUID;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public ArrayList<PlayerResultsResponse> getPlayerResults() {
		return playerResults;
	}
	public void setPlayerResults(ArrayList<PlayerResultsResponse> playerResults) {
		this.playerResults = playerResults;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
