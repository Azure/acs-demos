# Building the Trials Game

To build the Trials application...

1. `cd core`
2. `maven install`
3. `cd ../trials`
4. `maven clean package shade:shade`

To build a Docker container:

1. `docker build -t trials .`

# Running the Trials Game

To run (using Docker, after building container image)...

1. `docker run -it trials`

# Playing the Trials Game

See our game [documentation](intro.md) for an introduction 
to the game.