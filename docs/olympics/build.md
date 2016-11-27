# Building the Olympics Game

To build the Olympics application...

1. `cd core`
2. `maven install`
3. `cd ../olympics`
4. `maven clean package shade:shade`

To build a Docker container:

1. `docker build -t olympics .`

# Running the Olympics Game

To run (using Docker, after building container image)...

1. `docker run -it olympics`

# Playing the Olympics Game

See our game [documentation](intro.md) for an introduction 
to the game.