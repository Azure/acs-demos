# What is Spark?

Spark is a powerful general cluster computing system for Big Data. We will be using DC/OS to deploy a Spark cluster.


# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container Services cluster. Make sure you have the Azure CLI installed and have logged in.

```
az login
```

Next, we will create a resource group for the ACS cluster to be deployed.

```
az group create -n $CLUSTER_GROUP_NAME -l $CLUSTER_LOCATION
```

Now, we can create the cluster

```
az acs create --orchestrator-type=kubernetes -n $CLUSTER_NAME -g $CLUSTER_GROUP_NAME -d $CLUSTER_DNS_PREFIX --generate-ssh-keys
```

## Install the Kubernetes CLI

In order to manage this instance of ACS we will need the Kubernetes command line interface,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs kubernetes install-cli
```

## Initial Cluster Setup

First, we will load the master Kubernetes cluster configuration locally.

```
az acs kubernetes get-credentials --resource-group=$CLUSTER_GROUP_NAME --name=$CLUSTER_NAME
```

Next, we will use the Kubernetes command line interface to ensure the list of machines in the cluster are available.

```
kubectl get nodes
```

# Deploy Spark

We can create a new Spark cluster through the Kubernetes cli and the `yml` files found in the `k8s/spark` directory. We will also spin up Zeplin.

```
kubectl create -f k8s/spark
```

Once that is set up, ensure everything is running normally.

```
kubectl get pods
```

Next, we will check the load balancer endpoints.

```
kubectl get svc -o wide
```

## Enable the Kubernetes, Spark and Zeplin UI

To view the Kubernetes and Spark UI from a browser, we can use the cli to configure a proxy.

```
kubectl proxy --port=8001
```

Kubernetes will be available through http://localhost:8001/ui  
Spark will be available through http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/

For Zepplin, we can use the cli to configure a port forward and run it as a background process.

```
kubectl port-forward zeppelin-controller-[ID] 8080:8080 &
```

The Zeplin interface will be available through http://localhost8080
