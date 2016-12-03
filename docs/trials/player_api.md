Player API
--------------

### Game Status Update

When a player asks to join a game they initially receive a response indicating they are waiting for players to join the game. Once enough players have indicated they are available the engine informs players via a status update. Players must then acknowledge their ability to play within a set time period.

PUT /api/v0.1/player/game/status

{
  "gameID": "UID",
  "status": "ready"
}

Response (within 10 seconds, otherwise abort start of game):

{
  "gameID": "UID",
  "playerID": "UID",
  "status": "ready"
}

### Event Start

When a player is required to play their hand in an event this API is called. The player responds with the hand to be played.

POST /api/v0.1/player/event

{
  "gameID": "UID",
  "status": "start"
  "event": {
    "eventID" : "UID",
    "name": "EVENT NAME",
    "description": "Description of event"
  }
}

Response (within 10 seconds, otherwise the event is forfeit):

{  
  "gameId": "UID",
  "cards": [
    { 
      "name": "NAME",
      "traits": {
        "strength": 12,
        "speed": 10,
        "intelligence", 5
    }
  ]
}
     
## Event Result

When the engine has calculated the result for an event it notifies all players via this method.

PUT /api/v0.1/player/event

{
  "gameID": "UID",
  "status": "start"
  "event": {
    "eventID" : "UID",
    "result" : {
      "position": 3,
      "winner": {
        "player": {
          "playerID": "UID",
          "name": "NAME",
            "cards": [
		    { 
		      "name": "NAME",
		      "traits": {
		        "strength": 12,
		        "speed": 10,
		        "intelligence", 5
		    }
          ]
        }
        
    }        
  }
}
