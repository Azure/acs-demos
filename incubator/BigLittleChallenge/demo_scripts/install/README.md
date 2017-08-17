# Install Big Little Challenge onto a Azure Container Service (Kubernetes) Cluster

In this tutorial we weill install the ig Little Challenge application
onto an existing Azure Container Service Cluster using Kubernetes as
the orchestrator.

## Setup environment for Big Little Challenge

Here we will prepare an environment for running Big Little Challenge.

The currently defined environment variables are:

```
env | grep SIMDEM_*
```

If you are running in interactive mode simply continue and you will be
prompted for any mising values when necessary.

# Prerequisites

You must have
[deployed a Kubernetes cluster](../../../../kubernetes/create_cluster/README.md) 
in Azure Container Service onto which we will deploy the game.

Once the cluster is created you will need to install the Kubernetes
CLI and set up
the [management proxy](../../../../kubernetes/proxy/README.md) to the
cluster.

# Install Big Little Challenge

In this section we will install the application. it consists of a
number of different pods.

## Engine Deployment

The engine manages the game. It is the central controller for the game
as a whole.

```
kubectl create -f ../../kubernetes/engine-deployment.yaml
```

Results:

```
deployment "engine" created
```

```
kubectl create -f ../../kubernetes/engine-service.yaml
```

Results:

```
service "engine" created
```

## Dashboard Deployment

The dashboard is where we can monitor activity within the game.

```
kubectl create -f ../../kubernetes/dashboard-deployment.yaml
```

Results:

```
deployment "dashboard" created
```

```
kubectl create -f ../../kubernetes/dashboard-service.yaml
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
while [[ $DASHBOARD_IP == "" ]]; do sleep 2; DASHBOARD_IP=$(kubectl get service dashboard -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
echo $DASHBOARD_IP
```

At this point the the application Dashboard is available on port 8181
at the IP provided by the above command:

```
xdg-open http://$DASHBOARD_IP:8181
```

Results:

```
HTTP/1.0 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 2006
Server: Werkzeug/0.11.15 Python/3.6.0
Date: Tue, 25 Jul 2017 06:52:39 GMT
```






