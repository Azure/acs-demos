# Install Vamp on a Kubernetes ACS Cluster

We'll install Vamp on Azure Container Service with Kubernetes.

# What is Vamp?

[Vamp](http://vamp.io) is an open source solution that provides canary releasing and
autoscaling for microservices. It runs on Kubernetes, DC/OS and Docker
clusters. In this tutorial/demo we will focus on installing Vamp on
ACS with [Kubernetes](https://kubernetes.io).

## Prerequisites

We also need the [Azure CLI](../../../azure/login/README.md) installed
and we must be logged into our subscription.

We must have deployed and setup the connection to an [ACS (Kubernetes)
cluster](../../../kubernetes/proxy/README.md).)

This script uses Curl for some of it's validation testing, so lets
install that:

```
sudo apt-get install -qy curl
```

# Deploying Vamp on Kubernetes

We will start by installing Vamp's dependencies, etcd and elastic
search..

## Deploy etcd

First, we will set up etcd through the configuration file hosted
on
[GitHub](https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/etcd.yml).

```
kubectl create -f https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/etcd.yml
```

Results:

```
service "etcd-client" created
pod "etcd0" created
service "etcd0" created
pod "etcd1" created
service "etcd1" created
pod "etcd2" created
service "etcd2" created
```

## Deploy Elasticsearch

Next, we will set up a deployment of Elasticsearch configured to work
with Vamp.

```
kubectl run elasticsearch --image=elasticsearch:2.4.4
```

Results:

```
deployment "elasticsearch" created
```

```
kubectl expose deployment elasticsearch --protocol=TCP --port=9200 --name=elasticsearch
```

Results:

```
service "kibana" exposed
```

```
kubectl run kibana --image=kibana:4.6.4 --env="ELASTICSEARCH_URL=http://elasticsearch:9200"
```

Results:

```
deployment "kibana" created
```

```
kubectl expose deployment kibana --protocol=TCP --port=5601 --name=kibana
```

Results:

```
service "kibana" exposed
```

# Deploy Vamp

Now that we have the dependencies installed we can proceed with
installing Vamp itelf.

We will set up the Vamp gateway agent as a `daemon set` through the
configration file hosted
on
[GitHub](https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/vga.yml). A
DaemonSet ensures that all (or some) nodes run a copy of a pod. As
nodes are added to the cluster, pods are added to them. As nodes are
removed from the cluster, those pods are garbage collected. Deleting a
DaemonSet will clean up the pods it created.

```
kubectl create -f https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/vga.yml
```

Results:

```
daemonset "vamp-gateway-agent" created
service "vamp-gateway-agent" created
```

Next, we can deploy Vamp itself:

```
kubectl run vamp --image=magneticio/vamp:0.9.4-kubernetes
```

Results:

```
deployment "vamp" created
```


In order to be able to access Vamp we will expose port 8080.

```
kubectl expose deployment vamp --protocol=TCP --port=8080 --name=vamp --type="LoadBalancer"
```

Results:

```
service "vamp" exposed
```

## Wait for startup

Startup is relatively quick, but we do need to wait for the images to
be pulled onto the cluster and for the public IPs to register. This is
easily visible using `kubectl`:

```
kubectl get service
```

Results:

```
NAME                 CLUSTER-IP     EXTERNAL-IP     PORT(S)             AGE
elasticsearch        10.0.113.22    <none>          9200/TCP            16m
etcd-client          10.0.203.98    <none>          2379/TCP            20m
etcd0                10.0.0.250     <none>          2379/TCP,2380/TCP   20m
etcd1                10.0.132.130   <none>          2379/TCP,2380/TCP   20m
etcd2                10.0.18.90     <none>          2379/TCP,2380/TCP   20m
kibana               10.0.51.177    <none>          5601/TCP            16m
kubernetes           10.0.0.1       <none>          443/TCP             27m
vamp                 10.0.244.141   <pending>       8080:32010/TCP      12m
vamp-gateway-agent   10.0.113.212   40.71.183.146   80:32718/TCP        13m
```

Since we need to ensure our Public IPs have been assigned before
proceeding, and because we need the IP number later we'll run a loop
to grab the IP once assinged. This is a little cumbersome but great if
you want to script things. If you are doing this manually you can use
`kubectl get service --wait` to display changes as they happen.

```
VAMP_IP=""
while [ -z $VAMP_IP ]; do sleep 10; VAMP_IP=$(kubectl get service vamp -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
VAMP_GATEWAY_IP=""
while [ -z $VAMP_GATEWAY_IP ]; do sleep 10; VAMP_GATEWAY_IP=$(kubectl get service vamp-gateway-agent -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```


# Accessing the Vamp Interface

Now that Vamp is installed, the UI should be accessable through the
external IP address of the `vamp` service on port 8080:

```
curl --head $VAMP_IP:8080
```

Results:

```
HTTP/1.1 200 OK
Last-Modified: Fri, 14 Apr 2017 14:17:40 GMT
ETag: "1060015b6cd39fa0"
Accept-Ranges: bytes
Date: Tue, 25 Jul 2017 23:56:49 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 1544
```

Similarly, the vamp-gateway-agent should be accessible via port 80 on
it's own public IP:

```
curl --head $VAMP_GATEWAY_IP
```

Results:

```
HTTP/1.1 200 OK
Last-Modified: Fri, 14 Apr 2017 14:17:40 GMT
ETag: "1060015b6cd39fa0"
Accept-Ranges: bytes
Date: Tue, 25 Jul 2017 23:56:49 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 1544
```

