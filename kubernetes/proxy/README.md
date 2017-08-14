# Connect to the Kubernetes cluster

In order to interact with the Azure Container Service cluster we need
to install the Kubernetes CLI. This can be done through the Azure CLI:

# Install Kubernetes CLI

The Azure CLI simplifies installing the Kubernetes CLI tooling:

```
sudo az acs kubernetes install-cli
```

It can be convenient to have auto-completion:

```
source <(kubectl completion bash)
```

# Get Cluster Credentials

The Azure CLI also makes it easy to configure the Kuberenetes CLI
credentials for your cluster:

```
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```

# Configure Proxy to the Kubernetes Masters

The Kuberenetes CLI will setup a proxy to your cluster:

```
kubectl proxy --port=8001 &
```

# Validation

               
We can now interact with the cluster, for example we can get a list of
nodes it contains:

```
kubectl get nodes
```

Results:

```expected_similarity=0.2
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      1m
k8s-agent-14de76a8-1    Ready                      1m
k8s-agent-14de76a8-2    Ready                      1m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   1m
```

