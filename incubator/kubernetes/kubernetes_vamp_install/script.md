# What are we going to do?

We'll install Vamp on Azure Container Service with Kubernetes.

# What is Vamp?

[Vamp](http://vamp.io) is an open source solution that provides canary releasing and
autoscaling for microservices. It runs on Kubernetes, DC/OS and Docker
clusters. In this tutorial/demo we will focus on installing Vamp on
ACS with [Kubernetes](https://kubernetes.io).

It is assumed that you have prepared the demo environment by running `prep.sh`, if not you need to break from this script and run it now.

# Deploying Vamp

We will start by installing Vamp's dependencies.

## Deploy etcd

First, we will set up etcd through the configuration file hosted on [GitHub](https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/etcd.yml).

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

Next, we will set up a deployment of Elasticsearch configured to work with Vamp.

```
kubectl run elasticsearch --image=elasticsearch:2.4.4
kubectl run kibana --image=kibana:4.6.4 --env="ELASTICSEARCH_URL=http://elasticsearch:9200"
kubectl expose deployment elasticsearch --protocol=TCP --port=9200 --name=elasticsearch
kubectl expose deployment kibana --protocol=TCP --port=5601 --name=kibana

```

Results:

```
deployment "elasticsearch" created
deployment "kibana" created
service "elasticsearch" exposed
service "kibana" exposed
```

## Deploy Vamp

First, we will set up the Vamp gateway agent as a `daemon set` through the configration file hosted on [GitHub](https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/vga.yml).

```
kubectl create -f https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/vga.yml
```

Results:

```
daemonset "vamp-gateway-agent" created
service "vamp-gateway-agent" created
```

Next, we can deploy Vamp and expose it on port 8080.

```
kubectl run vamp --image=magneticio/vamp:0.9.4-kubernetes
kubectl expose deployment vamp --protocol=TCP --port=8080 --name=vamp --type="LoadBalancer"
```

Results:

```
deployment "vamp" created
service "vamp" exposed
```

Now, we can ensure that all the Kubernetes servies are running.

```
kubectl get services
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
vamp                 10.0.244.141   13.90.197.78    8080:32010/TCP      12m
vamp-gateway-agent   10.0.113.212   40.71.183.146   80:32718/TCP        13m
```

## Accessing the Vamp Interface

Now that Vamp is installed, the UI should be accessable through the external IP address of the `vamp` service on port 8080, in this case [http://13.90.197.78:8080](http://13.90.197.78:8080)