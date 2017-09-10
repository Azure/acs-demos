# Playing Big Little Challenge with only AI players

In this tutorial we will create a game of Big Little Challenge using
only AI players. This is useful for play testing.

## Prerequisites

We must first have installed the Big Little Challenge [game engine and Dashboard](../install/README.md).

## Deploy the first AI player

The AI Player is available so that we can ensure there are always
enough players in the system to make it interesting for human players.

The default setup of the game when installed is to require 2 players
before the game can start. We'll start by adding one player and then
scale to 2.

```
kubectl create -f ../../kubernetes/aiplayer-deployment.yaml
```

Results:

```
deployment "aiplayer" created
```

```
kubectl create -f ../../kubernetes/aiplayer-service.yaml
```

Results:

```
service "aiplayer" created
```

Once the player container has started up we will see that they are
assigned a game ticket in the Engine. Note, this might take a minute
if this is the first time a player has been deployed to the agent it
is scheduled to, otherwise it will be a few seconds.

```
kubectl logs deployment/engine --tail=1
```

## Scale the aiplayer service

Scaling to two players is a simple matter of running a scale command:


```
kubectl scale --replicas=2 deployment/aiplayer
```

Results:

```
deployment "aiplayer" scaled
```

The current status of the scale operation is retrieved equally as
easily.

```
kubectl get deployment/aiplayer
```

Results:

```
NAME       DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
aiplayer   2         2         2            1           21m
```

Once both players have registered with the game engine the game will
start. You can watch it's progress on the game dashboard, this is
available on a Public IP that we can retrieve using the Kubernetes CLI
(see the [install](../install/README.md) instructions for details of
how).


```
xdg-open http://$DASHBOARD_IP:8181
```

