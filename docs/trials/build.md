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


## On A Swarm Mode Cluster

After the above build steps, including pushing to Docker Hub...

Swarm Mode clusters do not support docker-compose unless deploying to
a single host. Therefore we need to use Swarm Mode serbvices instead:

The scripts `run.sh` and `run-arm.sh` will run the application as
docker containers.

Note, by default these scripts start a 5 plauer game but only create 2
players, this means it is necessary to sale the aiplayer service to
actually start the game:

`docker service scale aiplayer=5`

Check the logs of the services (this is harder than it should be, need
to put a REST API to get useful info)

  * Find the node the container of interest is running on with `docker service ps engine`
  * Log into the node * Find the ID of the container with `docker ps`
  * Check the logs with `docker logs ID`

## Larger games

If you want to play larger games you can change the number of
required players with the `MIN_NUMBER_OF_PLAYERS` environemnt
variable in the engine service commands above.

# Playing the Trials Game

See our game [documentation](intro.md) for an introduction 
to the game.
