# The Big Little Challenge

This demo is a card game framework. It provides a complete game
called [Trials](docs/trials/intro.md) which is a deck building game
with elements of stats management.

Games are deployable as Docker containers. This means you can run the
games anywhere that Docker containers can be run, such as Azure
Container Service. 

## Azure Container Service

The game is tested on Azure Container Service in both Swarm Mode and
Kuberntes.

## PiSwarm: A Docker Swarm Cluster

When running in Swarm Mode it is possible to run the game on a cluster
of Raspberry Pis. This is a great demo of the portbility of
applications from Azure Container Service to commodity hardware in
your on premise environment. We don't really expect people to run on a
Raspberry Pi cluster, but the fact that you can is a powerful
demonstration. You can find instructions on how to build
a [Raspberry Pi Docker Cluster](docs/pi/piswarm.md) within this
project.

