# Building the Trials Game

To build the Trials application...

1. Install Docker 1.12+
4. Run `build.sh` to build the i386 version or `build-arm.sh` to build the ARM version(Raspberry Pi)
  1. If you are using Windows we don't currently have a script, simply open the build file and run the Docker build commands within
5. [OPTIONAL] push the containers to Docker Hub using `publish.sh` or `publish-arm.sh` (required if you want to deploy to a cluster of hosts)

# Running the Trials Game

If you are running on a single host (e.g. your dev machine) then use
the Docker Compose method described in the first section below. If you
are deploying to a Swarm cluster then use the Swarm Mode services
method described in the second sectio.

## Using Docker Compose

After the above build steps you can run the game engine with:

`docker-compose up -d` 

This will start a game engine for Trials, it will also start a 
single AI Player. The game requires two players before it will 
start. You can create an additional AI plyaer with:

`docker-compose scale aiplayer=2`

After running this command a second player will register with 
game engine and the game will start as a result.

At the time of writing a complete game will be played with details
output in the log. To view the logs for all containers use:

`docker-compose logs`

To view the engine dashbaord visit http://MY_HOST:8181

## On A Swarm Mode Cluster

To be able to use Docker Compose v3 on a Swarm Mode cluster you need to run Docker 1.13+.
After the above build steps, including pushing to Docker Hub...

Docker Compose has been integrated into the Docker CLI and you can run the application using the following command:

`docker stack deploy biglittlechallenge -c docker-compose-v3.yml`

*Make sure to use the v3 compose file that is in the root folder*

Note, by default these scripts start a 5 plauer game but only create 2
players, this means it is necessary to sale the aiplayer service to
actually start the game:

`docker service scale biglittlechallenge_aiplayer=5`

To view the engine dashbaord visit http://MY_HOST:8181

Check the logs of the services (this is harder than it should be, need
to put a REST API to get useful info)

  * Find the node the container of interest is running on with `docker service ps biglittlechallenge_engine`
  * Log into the node * Find the ID of the container with `docker ps`
  * Check the logs with `docker logs ID`

To stop the application use:

`docker stack rm biglittlechallenge`

## Larger games

If you want to play larger games you can change the number of
required players with the `MIN_NUMBER_OF_PLAYERS` environemnt
variable in the engine service commands above.

# Playing the Trials Game

See our game [documentation](intro.md) for an introduction 
to the game.
