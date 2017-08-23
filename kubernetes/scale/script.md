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
NAME                    STATUS                     AGE       VERSION
k8s-agent-8ff9783f-0    Ready                      1m        v1.6.6
k8s-agent-8ff9783f-1    Ready                      1m        v1.6.6
k8s-agent-8ff9783f-2    Ready                      1m        v1.6.6
k8s-master-8ff9783f-0   Ready,SchedulingDisabled   7m        v1.6.6
```

## Scale Up

```
az acs scale --new-agent-count 4 --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP
```

Wait for nodes to come online:

```
sleep 300
```

Check we have more nodes:

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE       VERSION
k8s-agent-8ff9783f-0    Ready                      2m        v1.6.6
k8s-agent-8ff9783f-1    Ready                      2m        v1.6.6
k8s-agent-8ff9783f-2    Ready                      3m        v1.6.6
k8s-agent-8ff9783f-3    Ready                      3m        v1.6.6
k8s-master-8ff9783f-0   Ready,SchedulingDisabled   13m       v1.6.6
```


## Scale Down

```
az acs scale --new-agent-count 3 --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP
```

Wait for nodes to shutdown:

```
sleep 300
```

Check we have less nodes:

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE       VERSION
k8s-agent-8ff9783f-0    Ready                      2m        v1.6.6
k8s-agent-8ff9783f-1    Ready                      2m        v1.6.6
k8s-agent-8ff9783f-2    Ready                      3m        v1.6.6
k8s-master-8ff9783f-0   Ready,SchedulingDisabled   13m       v1.6.6
```

