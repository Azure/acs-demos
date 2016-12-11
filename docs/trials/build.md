# Building the Trials Game

To build the Trials application...

1. Install JDK
2. Install Maven
3. Install Docker
4. Run `build.sh`

# Running the Trials Game

If you run `docker-compose up -d` (after the above build steps) you
will start an game of Trials in which you have two AI players. Updates
to the status of the game can be viewed with:

`curl http://localhost:8080/api/v0.1/tournament/status`	

# Playing the Trials Game

See our game [documentation](intro.md) for an introduction 
to the game.