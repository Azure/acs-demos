# Deploying ACS Engine Generated Templates

The Azure Container Service Engine (acs-engine) generates ARM (Azure
Resource Manager) templates for Docker enabled clusters on Microsoft
Azure with your choice of DCOS, Kubernetes, or Swarm
orchestrators. The input to acs-engine is a cluster definition file
which describes the desired cluster, including orchestrator, features,
and agents. The structure of the input files is very similar to the
public API for Azure Container Service.

# Prerequisites

It is assumed that you
have [prepared your environment](../preparation/script.md)
and [generated templates](../generate/script.md) for the cluster you
want to deploy. If not please follow the instructions linked.

### Deploy using Azure CLI

```
az group deployment create --resource-group $ACSE_RESOURCE_GROUP --template-file $ACSE_WORKSPACE/_output/azuredeploy.json --parameters $ACSE_WORKSPACE/_output/azuredeploy.parameters.json
```

## Interacting with the Cluster

The cluster has now been created. In order to interact with the it we
need to install the Kubernetes CLI. This can be done through the Azure
CLI:

```
sudo az acs kubernetes install-cli
```

In order to connect to the cluster we need to get the credentials from
ACS.

```
az acs kubernetes get-credentials --resource-group=$SIMDEM_RESOURCE_GROUP --name=$SIMDEM_CLUSTER_NAME
```
               
We can now interact with the cluster, for example we can get a list of
nodes.

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

