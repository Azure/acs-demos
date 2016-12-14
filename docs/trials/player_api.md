Player API
--------------

### Game Status

#### Read (GET)

Get the current status of the player with respect to their current game.

GET /api/v0.1/player/game/status

Example response:

{
  "gameID": "UID",
  "playerID": "UID",
  "status": "playing"
}

### Game Starting (PUT)

When a player asks to join a game they initially receive a response indicating they are 
waiting for players to join the game. Once enough players have indicated they are 
available the engine informs players via a Game Starting call. Players must then acknowledge 
their ability to play within a set time period.

PUT /api/v0.1/player/game/start

Payload:

{
  "gameID": "UID",
  "playerID": "UID",
  "status": "ready",
  "tournament": { [TOURNAMENT RULES] }
}

Response (within 10 seconds, otherwise abort start of game):

{
  "gameID": "UID",
  "playerID": "UID",
  "status": "ready"
}

### Rounds (Events in a Trials Tournament)

An event is a single hand in the game. Players will select the cards to play and respond accordingly.

#### Play Round (GET)

When a player is required to play their hand in an event this API is called.
The player responds with the hand to be played.

GET /api/v0.1/player/cards?gameID=UID&roundID=UID

Response (within 10 seconds, otherwise the event is forfeit):

{  
  "gameID": "UID",
  "roundID": "UID"
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
     
## Round Result

When the engine has calculated the result for a hand it notifies all players via this method.

PUT /api/v0.1/player/round/result

Payload:

{
  "gameID": "UID",
  "hand": {
    "handID" : "UID",
    "playerPositions" : [
      ...      
    ]        
  }
}

Response:

{
  "gameID": "UID",
  "playerID": "UID",
  "status": "Playing"
}