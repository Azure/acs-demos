# Create a K8 connector 

Here we will create a K8 connector which acts as a bridge between
Azure Container Instances and an Azure Container Service K8 cluster.

## Prerequisites

We need a [service principle](../../../../azure/service_principle/) that
the ACI connector will use to manage ACI resources.

You will need a Kubernetes cluster and have set up
a [management proxy to the cluster](../../../../kubernetes/proxy).

## Deploy the ACI Connector

```
cat examples/aci-connector.yaml
```

Results:

```expected_similarity=0.5
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
          value: $SIMDEM_APP_ID
        - name: AZURE_CLIENT_KEY
          value:
        - name: AZURE_TENANT_ID
          value: $SIMDEM_TENANT_ID
        - name: AZURE_SUBSCRIPTION_ID
          value: $SIMDEM_SUBSCRIPTION_ID
        - name: ACI_RESOURCE_GROUP
          value: $SIMDEM_RESOURCE_GROUP
```

Lets create the connector using the Kubernetes CLI:

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
while [ -z $NGINX_IP ]; do sleep 10; NGINX_IP=$(kubectl get service nginx -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```

Now we have our IP:

```
echo $NGINX_IP
```

Take a look...

```
xdg-open $NGINX
```

