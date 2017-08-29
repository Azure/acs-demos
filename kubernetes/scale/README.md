# Scale a Kubernetes cluster

Here we will scale a pre-existing Azure COntainer Service (Kubernetes)
cluster.

## Prerequisites

We need to have [created and connected to an ACS (Kubernetes) cluster](../proxy/README.md).

## Check the current number of nodes

Lets see how mant nodes we currently have.

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
az acs scale --new-agent-count 4 --name $SIMDEM_CLUSTER_NAME --resource-group $SIMDEM_RESOURCE_GROUP
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
az acs scale --new-agent-count 3 --name $SIMDEM_CLUSTER_NAME --resource-group $SIMDEM_RESOURCE_GROUP
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

