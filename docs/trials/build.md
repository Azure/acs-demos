# Building the Trials Game

To build the Trials application...

1. Install JDK
2. Install Maven
3. Install Docker
4. Run `build.sh`

# Running the Trials Game

After the above build steps you can run the game engine with:

`docker-compose up -d` 

This will start a game engine for Trials, it will also start a 
single AI Player. The game requires two players before it will 
start. You can create an additional AI plyaer with:

`docker-compose scale aiplayer=2`

After running this command a second player will register with 
game engine and the game will start as a result.

At the time of writing a complete game will be played with
details output in the log.

## Larger games

If you want to play larger games you can change the number of
required players with the `MIN_NUMBER_OF_PLAYERS` environemnt
variable in the `docker-compose.tml` file.

# Playing the Trials Game

See our game [documentation](intro.md) for an introduction 
to the game.