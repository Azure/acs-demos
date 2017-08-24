# Create a K8 connector 

Here we will create a K8 connector which acts as a bridge between
Azure Container Instances and an Azure Container Service K8 cluster.

## Prerequisites

You will need a Kubernetes cluster and have set up
a
[management proxy to the cluster](../../../kubernetes/proxy/README.md).

You also must have jq which you can install with:

```
sudo apt-get install jq
```

## Create a Resource Group
The ACI Connector will create each container instance in a specified
resource group. You can create a new resource group with:

```
az group create -n $SIMDEM_RESOURCE_GROUP -l $SIMDEM_LOCATION
```

## Create a Service Principal

A service principal is required to allow the ACI Connector to create
resources in your Azure subscription. You can create one using the az
CLI using the instructions below.  Find your subscriptionId with the
az CLI:

Use az to create a Service Principal that can perform operations on
your resource group:

```
SUBSCRIPTION_ID=$(az account show | jq -r '.id')
SP_JSON=$(az ad sp create-for-rbac --role="Contributor" --scopes="/subscriptions/$SUBSCRIPTION_ID/resourceGroups/$SIMDEM_RESOURCE_GROUP")
```

## Install the ACI Connector

```
cat examples/aci-connector.yaml
```

Results:

```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: aci-connector
  namespace: default
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: aci-connector
    spec:
      containers:
      - name: aci-connector
        image: microsoft/aci-connector-k8s:latest
        imagePullPolicy: Always
        env:
        - name: AZURE_CLIENT_ID
          value: $(echo $SP_JSON | jq -r '.appId') 
        - name: AZURE_CLIENT_KEY
          value: $(echo $SP_JSON | jq -r '.password')
        - name: AZURE_TENANT_ID
          value: $(az account show | jq -r '.tenantId')
        - name: AZURE_SUBSCRIPTION_ID
          value: $(az account show | jq -r '.id')
        - name: ACI_RESOURCE_GROUP
          value: $SIMDEM_RESOURCE_GROUP
```

```
kubectl create -f examples/aci-connector.yaml 
```

Check the connector provides a new "node", this is not really a node,
it's more of a near infinite compute resource.

```
kubectl get nodes
```

## Install the NGINX example

```
cat examples/nginx-pod.yaml
```

Results:

```
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  namespace: default
spec:
  containers:
  - image: nginx
    imagePullPolicy: Always
    name: nginx
  dnsPolicy: ClusterFirst
  nodeName: aci-connector
```

```
kubectl create -f examples/nginx-pod.yaml 
```

```
kubectl get pods -o wide
```

Since we need to ensure our Public IPs have been assigned before
proceeding, and because we need the IP number later we'll run a loop
to grab the IP once assinged. This is a little cumbersome but great if
you want to script things. If you are doing this manually you can use
`kubectl get service --wait` to display changes as they happen.

```
NGINX_IP=""
while [ -z $NGINX_IP ]; do sleep 10; NGINX_IP=$(kubectl get service vamp -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```

Now we have our IP:

```
echo $NGINX_IP
```

Take a look...

```
xdg-open $NGINX
```

