# The Big Little Challenge

This demo is a card game framework. It provides a complete game
called [Trials](docs/trials/intro.md) which is a deck building game
with elements of stats management.

Games are deployable as Docker containers. This means you can run the
games anywhere that Docker containers can be run, such as Azure
Container Service. 

## Build

For convenience the complete application will build inside containers. 
First ensure you have Docker running, then run `./build.sh`.

This is not the most efficient way of building the container as all 
build artifacts are included in the image. Once day we'll fix this, 
but for now it's an easy way to get started.

## Run

As a containerized application you can run it anywhere containers will 
run. We provide Docker (use `docker-compose up -d`) and Kubernetes 
(use `./kubernetes/run.sh`) configuraiton files.

In the default configuration the game starts up with two "AI" players 
(they don't really contain any AI, they simply select a card at random).
You can view the application at `http://localhost:8181` (or the 
appropriate host if deploying remotely).

For more information on how the game works see the `docs/trials/` folder.

## Azure Container Service

The game is tested on Azure Container Service in both Swarm Mode and
Kubernetes.

## PiSwarm: A Docker Swarm Cluster

When running in Swarm Mode it is possible to run the game on a cluster
of Raspberry Pis. This is a great demo of the portbility of
applications from Azure Container Service to commodity hardware in
your on premise environment. We don't really expect people to run on a
Raspberry Pi cluster, but the fact that you can is a powerful
demonstration. You can find instructions on how to build
a [Raspberry Pi Docker Cluster](docs/pi/piswarm.md) within this
project.

