# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container Services cluster. Make sure you have the Azure CLI installed and have logged in.

```
az login
```

Next, we will create a resource group for the ACS cluster to be deployed.

```
az group create --name acs-k8s-spark-demo --location eastus
```

Results:

```
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/acs-k8s-spark-demo",
  "location": "eastus",
  "managedBy": null,
  "name": "acs-k8s-spark-demo",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null
}
```

Now, we can create the cluster

```
az acs create --orchestrator-type=kubernetes --name acs-k8s-spark-cluster --resource-group acs-k8s-spark-demo --dns-prefix acs-k8s-spark-dns --generate-ssh-keys
```

Results:

```
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/acs-k8s-spark-demo/providers/Microsoft.Resources/deployments/azurecli1496363170.3581209",
  "name": "azurecli1496363170.3581209",
  "properties": {
    "correlationId": "d9ac5c3f-83ec-4d30-861f-5c331f8ac40b",
    "debugSetting": null,
    "dependencies": [],
    "mode": "Incremental",
    "outputs": null,
    "parameters": {
      "clientSecret": {
        "type": "SecureString"
      }
    },
    "parametersLink": null,
    "providers": [
      {
        "id": null,
        "namespace": "Microsoft.ContainerService",
        "registrationState": null,
        "resourceTypes": [
          {
            "aliases": null,
            "apiVersions": null,
            "locations": [
              "eastus"
            ],
            "properties": null,
            "resourceType": "containerServices"
          }
        ]
      }
    ],
    "provisioningState": "Succeeded",
    "template": null,
    "templateLink": null,
    "timestamp": "2017-06-02T00:32:36.607132+00:00"
  },
  "resourceGroup": "acs-k8s-spark-demo"
}
```

## Install the Kubernetes CLI

In order to manage this instance of ACS we will need the Kubernetes command line interface,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs kubernetes install-cli
```

Results:

```
Downloading client to /usr/local/bin/kubectl from https://storage.googleapis.com/kubernetes-release/release/v1.6.4/bin/linux/amd64/kubectl
```

## Initial Cluster Setup

First, we will load the master Kubernetes cluster configuration locally.

```
az acs kubernetes get-credentials --resource-group=acs-k8s-spark-demo --name=acs-k8s-spark-cluster
```

Results:

```
[No output on success]
```

Next, we will use the Kubernetes command line interface to ensure the list of machines in the cluster are available.

```
kubectl get nodes
```

Results:

```
NAME                    STATUS                     AGE       VERSION
k8s-agent-b90bd903-0    Ready                      1h        v1.5.3
k8s-agent-b90bd903-1    Ready                      1h        v1.5.3
k8s-agent-b90bd903-2    Ready                      1h        v1.5.3
k8s-master-b90bd903-0   Ready,SchedulingDisabled   1h        v1.5.3
```