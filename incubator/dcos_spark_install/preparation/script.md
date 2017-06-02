# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container Services cluster. Ensure you have the Azure CLI installed and have logged in.

```
az login
```

Next, we will create a resource group for the ACS cluster to be deployed.

```
az group create --name $RESOURCE_GROUP --location $REGION
```

Results:

```
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
az acs create --name $CLUSTER_NAME --resource-group $RESOURCE_GROUP --dns-prefix $DNS_PREFIX --generate-ssh-keys
```

Results:

```
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

## Install the DC/OS CLI

In order to manage this instance of ACS we will need the DC/OS cli,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs dcos install-cli
```

Results:

```
Downloading client to /usr/local/bin/dcos
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel, allowing us to view the DC/OS UI on our local machine.

```
sudo ssh -fNL 80:localhost:80 -p 2200 azureuser@acs-dcos-spark-dnsmgmt.eastus.cloudapp.azure.com -i ~/.ssh/id_rsa
```

Now we tell the DC/OS CLI to use this tunnel to communicate with the cluster.

```
dcos config set core.dcos_url http://localhost
```

Results:

```
[core.dcos_url]: changed from 'http://localhost:80' to 'http://localhost'
```

At this point, the DC/OS interface should be available at [https://localhost](https://localhost).
