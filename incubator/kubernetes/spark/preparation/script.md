# Azure Subscription

You will need an active azure subscription. Before proceeding with
this script ensure that you are logged in using `az login`. Assuming
you started this script with the `prep.sh` script this is a safe
assumption to make.

# Environment Setup

Since we will be creating an ACS cluster it is important that we first
setup the environment to be unique to you, otherwise we will get
naming conflicts between people running the tutorials. 

You can do this through interactive variables here, or you can set
values in a local `env.local.json` file. We recommend that you start
by copying the existing `env.json` file.

If you are running in interactive mode simply continue and you will be
prompted for values when necessary.

# Installing required software for the demo

We need to ensure all the right software is available for us on the
current machine:

```
sudo apt-get update
sudo apt-get install openssh-client -y
sudo apt-get install git -y
```

# Ensuring we have a clean cluster

It's always wise to ensure that a demo starts in a clean state. To
that end we will delete any existing cluster and SSH infromation that
exists using this configuration. Don't worry if this command returns a
"could not be found" error. It just means you didn't have anything
dangling after the last demo.

```
az group delete --name $ACS_RESOURCE_GROUP --yes
sudo ssh-keygen -f "/root/.ssh/known_hosts" -R [${ACS_DNS_PREFIX}mgmt.$ACS_REGION.cloudapp.azure.com]:2200
```

# Creating a Cluster

We can now create a resource group that will contain all the Azure resouces deployed by ACS.

```
az group create --name $ACS_RESOURCE_GROUP --location $ACS_REGION
```

Results:

```expected_similarity=0.3
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
az acs create --orchestrator-type=kubernetes --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP --dns-prefix ${ACS_DNS_PREFIX} --generate-ssh-keys
```

Results:

```expected_similarity=0.05
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

## Connect to the cluster

Grab the cluster credentials using the Azure CLI:

```
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```

Verify we are connected to the cluster by inspecting the nodes available to us:

```
kubectl get nodes
```

Results:

```expected_similarity=0.1
NAME                    STATUS                     AGE       VERSION
k8s-agent-b90bd903-0    Ready                      1h        v1.5.3
k8s-agent-b90bd903-1    Ready                      1h        v1.5.3
k8s-agent-b90bd903-2    Ready                      1h        v1.5.3
k8s-master-b90bd903-0   Ready,SchedulingDisabled   1h        v1.5.3
```

## Get the Kubernetes Spark Examples

The Kubernetes [GitHub repository](https://github.com/kubernetes/kubernetes) includes helpful examples to get started. One of which, is a set of configuration files for Spark (`examples/spark`).

Let's clone the repository so that we can install Spark and Zeppelin easier later on.

```
mkdir -p ~/.acs/demo
git clone https://github.com/kubernetes/kubernetes.git ~/.acs/demo/kubernetes
```

Now, we are all set up and ready to run the main demo script and spin up an instance of Spark and Zeppelin on Kubernetes.

# Next Steps

  1. [Install Apache Spark](install/script)
  2. [Delete the cluster](cleanup/script.md)
