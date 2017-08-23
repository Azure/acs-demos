# Create a K8 connector 

Here we will create a K8 connector which acts as a bridge between Azure Container Instances and an Azure Container Service K8 cluster. 

##

You must have deployed a [Kubernetes cluster] (../../kubernetes/create/).

You also must have jq which you can install with sudo apt-get install jq


## Create a Resource Group
The ACI Connector will create each container instance in a specified resource group. You can create a new resource group with:

```
az group create -n k8connector-rg -l westus
```

## Create a Service Principal

A service principal is required to allow the ACI Connector to create resources in your Azure subscription. You can create one using the az CLI using the instructions below.
Find your subscriptionId with the az CLI:

```
az account list -o table
```
Use az to create a Service Principal that can perform operations on your resource group:

```
SP_JSON=$(az ad sp create-for-rbac --role="Contributor" --scopes="/subscriptions/"$(az account show | jq -r '.id')"/resourceGroups/k8connector-rg")
```
## Edit examples/aci-connector.yaml 
Edit the examples/aci-connector.yaml and input environment variables using the values above:
AZURE_CLIENT_ID: insert appId
AZURE_CLIENT_KEY: insert password
AZURE_TENANT_ID: insert tenant
AZURE_SUBSCRIPTION_ID: insert subscriptionId

## Install the ACI Connector

```
kubectl create -f incubator/aci/create-k8-connector/examples/aci-connector.yaml 
```
deployment "aci-connector" created

```
kubectl get nodes
```

## Install the NGINX example
```
kubectl create -f incubator/aci/create-k8-connector/examples/nginx-pod.yaml 
```
pod "nginx" created 
```
kubectl get po -o wide
```
This pod tolerates the taint on aci so it can be scheduled onto aci
```
kubectl create -f incubator/aci/create-k8-connector/examples/nginx-pod-tolerations.yaml
```
Note that if you have other nodes in your cluster then this Pod may not necessarily schedule onto the Azure Container Instances.
To force a Pod onto Azure Container Instances, you can either explicitly specify the NodeName as in the first example, or you can delete all of the other nodes in your cluster using kubectl delete nodes <node-name>.
