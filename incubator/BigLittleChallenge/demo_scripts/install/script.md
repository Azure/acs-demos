# Inatall Big Little Challenge onto a Azure Container Service (Kubernetes) Cluster

In this tutorial we weill install the ig Little Challenge application
onto an existing Azure Container Service Cluster using Kubernetes as
the orchestrator.

## Setup environment for Big Little Challenge

Here we will prepare an environment for running Big Little Challenge.

The currently defined environment variables are:

```
env | grep ACS_*
```

If you are running in interactive mode simply continue and you will be
prompted for any mising values when necessary.


### Validate cluster

We need to ensure we have a working Kubernetes cluster. We can check
that the cluster is available using the Azure CLI as follows:

```
az acs show -g $ACS_RESOURCE_GROUP -n $ACS_CLUSTER_NAME --output tsv --query provisioningState
```

Results:

```
Succeeded
```

If this says anything other than "Succeeded" you will need to ensure
that the cluster is correctly created. If it says "Provisioning" wait
a little longer before proceeding. If you need to create a cluster see
the [tutorial / demo](../../preparation/script.md).

## Connect to the Kubernetes cluster

In order to interact with the Azure Container Service cluster we need
to install the Kubernetes CLI. This can be done through the Azure CLI:

```
sudo az acs kubernetes install-cli
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
source <(kubectl completion bash)
kubectl proxy --port=8001 &
```

# Installing Big Little Challenge

In this section we will install the application. it consists of a
number of different pods.

## Engine Deployment

The engine manages the game. It is the central controller for the game
as a whole.

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/engine-deployment.yaml
```

Results:

```
Starting to serve on 127.0.0.1:8001 deployment "engine" created
```

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/engine-service.yaml
```

Results:

```
service "engine" created
```

## AI Player Deployment

The AI Player is available so that we can ensure there are always
enough players in the system to make it interesting for human players.

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/aiplayer-deployment.yaml
```

Results:

```
deployment "aiplayer" created
```

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/aiplayer-service.yaml
```

Results:

```
service "aiplayer" created
```

## Dashboard Deployment

The dashboard is where we can monitor activity within the game.

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/dashboard-deployment.yaml
```

Results:

```
deployment "dashboard" created
```

```
kubectl create -f ${SIMDEM_CWD}../../kubernetes/dashboard-service.yaml
```

Results:

```
service "dashboard" created
```

## Wait for startup

Startup is relatively quick, but we do need to wait for the images to
be pulled onto the cluster and for the public IP to register. This is
easily visible using `kubectl`:

```
kubectl get service
```

Since we need to ensure our Public IP has been assigned before
proceeding, and because we need the IP number later we'll run a loop
to grab the IP once assinged. This is a little cumbersome but great if
you want to script things. If you are doing this manually you can use
`kubectl get service --wait` to display changes as they happen.

```
DASHBOARD_IP=""
while [ -z $DASHBOARD_IP ]; do sleep 10; DASHBOARD_IP=$(kubectl get service dashboard -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```

At this point the the application Dashboard is available on port 8181
at the IP provided by the above command:

```
curl --head $DASHBOARD_IP:8181
```

Results:

```
HTTP/1.0 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 2006
Server: Werkzeug/0.11.15 Python/3.6.0
Date: Tue, 25 Jul 2017 06:52:39 GMT
```






