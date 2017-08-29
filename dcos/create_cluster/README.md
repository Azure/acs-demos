# Create an ACS DC/OS cluster

Here we will create a DC/OS cluster.

# Prerequisites

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll also need to
be [logged in to Azure](../../azure/login/README.md).

## Dependencies

There are a few dependencies that we must have installed for this
tutorial/demo to work:

```
sudo pip3 install virtualenv
sudo apt-get install openssh-client -y
```

## Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container
Services cluster. First, we will create a resource group for the ACS
cluster to be deployed.

```
az group create --name $SIMDEM_RESOURCE_GROUP --location $SIMDEM_LOCATION
```

Results: 

```Expected_similarity=0.4
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/acs-dcos-spark-demo",
  "location": "eastus",
  "managedBy": null,
  "name": "acs-dcos-spark-demo",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null
}
```

Now, we can create the cluster.

```
az acs create --name $SIMDEM_CLUSTER_NAME --resource-group $SIMDEM_RESOURCE_GROUP --dns-prefix $SIMDEM_DNS_PREFIX --agent-count 6 --generate-ssh-keys
```

Results:

```Expected_similarity=0.005
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/acs-dcos-spark-demo/providers/Microsoft.Resources/deployments/azurecli1496363170.3581209",
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
  "resourceGroup": "acs-dcos-spark-demo"
}
```

# Validation

You can check that the cluster is available using the Azure CLI as
follows:

```
az acs show -g $SIMDEM_RESOURCE_GROUP -n $SIMDEM_CLUSTER_NAME --query provisioningState
```

Results:

```
"Succeeded"
```

If this says anything other than "Succeeded" you will need to ensure
that the cluster is correctly created. If it says "Provisioning" wait
a little longer before proceeding.

# Next Steps

You are now setup to create workloads on DC/OS.

  1. [Install Apache Spark and Apache Zeppelin](../dcos_spark_install/README.md)
  2. [Delete the ACS DC/OS cluster](../cleanup/README.md)
