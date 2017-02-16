Engine API
--------------

## Status

To get the status of a game in progress.

GET /api/v0.1/tournament/status

## Join a game

To join a game a player posts a request to the engine.

POST /api/v0.1/tournament/join

Payload
```json
{
   "playerID": "UID",
   "name": "PLAYER_NAME",
   "api": "PLAYER_API_URL"
}
```

Response will indicate that a game is joined and we are waiting for it to start. Once the start conditions are met (initially a minimum number of players) each player will be notified with a REST API call and, upon acknowledgement (see below) the game will start.

```json
{
  "gameID": "UID",
  "status": "waiting for players"
}



