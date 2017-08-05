# Scale a Kubernetes cluster

Here we will scale a pre-existing Azure COntainer Service (Kubernetes)
cluster.

## Environment Setup

The currently defined environment variables are:

```
env | grep ACS_*
```

If you are running in interactive mode simply continue and you will be
prompted for any mising values when necessary.


## Prerequisites

We need to have [created an ACS (Kubernetes) cluster](https://raw.githubusercontent.com/Azure/acs-demos/master/kubernetes/create_cluster/script.md).

## Connect to the cluster

In order to connect to the cluster we need to get the credentials from
ACS.

```
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```

## Check the current number of nodes

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      1m
k8s-agent-14de76a8-1    Ready                      1m
k8s-agent-14de76a8-2    Ready                      1m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   1m
```

## Scale Up

```
az acs scale --new-agent-count 4 --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP
```

Check we have more nodes:

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      6m
k8s-agent-14de76a8-1    Ready                      6m
k8s-agent-14de76a8-2    Ready                      6m
k8s-agent-14de76a8-3    Ready                      1m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   6m
```


## Scale Down

```
az acs scale --new-agent-count 3 --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP
```

Check we have less nodes:

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      10m
k8s-agent-14de76a8-1    Ready                      10m
k8s-agent-14de76a8-2    Ready                      10m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   10m
```

