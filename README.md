# The Big Little Challenge

The goal of this project is to introduce young and old programmers to
open source collaboration while building a game. It's about fun and
learning. The code outputs are secondary. It's a playground with
enough structure around it to provide common ground for participants.

# Status

The original idea was pitched to a small audience at SEAGL, a community run conference
in Seattle, Nov 12 2016. It generated enough interest to spark some work and that's 
where we are at right now. See sections below for a little more.

We look forward to your involvement.


# Building a Game

At the simplest level we are building card games. They are simple
games. They aren't intended to be a AAA best seller, or even a bargain
bin item. We are creating them for fun, so lets enjoy. 

See our ['Generalized Description of Card Games'](../../wiki/Card-Game-Treatise).

Of course, if we happen to produce something valuable it's open
source, so go do what you can with it.

## Games

Here's some game ideas:

1. [Trials](docs/trials/intro.md) - a deck building game with elements of stats management. Initial rules and the very begining of an application to run it
2. [Narhumo](../../wiki/Narhumo-Rules) - a building and resource management game, outline rules only
3. [Sharks and Sands](../../wiki/Hen-Game::Sharks-and-Sands) - a simple introductory game, initial rules available
	
We welcome playtesters, rules modifications, code to run the games and new games. We also welcome any other contribution you would like to make :-)

## Hosting a game

Any games implemented in software will be deployable as Docker containers. This means you can run the games
anywhere that Docker containers can be run. However, it's much more
fun to build your own "data center" from Raspberry Pis and run it there:

### PiSwarm: A Docker Swarm Cluster

This Docker Swarm cluster is the "datacenter" we have actually built, see [Raspberry Pi
Docker Swarm Cluster](docs/pi/piswarm.md).

### PiKube: A Kubernetes Cluster

This is very similar to the Docker Swarm Cluster but is less complete in that we haven't actually built it yet. We do have some [notes](docs/pi/pikube.md) for those who want to tackle it though.
# biglittlechallenge
# biglittlechallenge
