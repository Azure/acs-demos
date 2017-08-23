# Create an ACS (Kubernetes) Cluster

Here we will create a Kubernetes cluster in Azure Container Service.

## Environment Setup

Since we will be creating an ACS cluster it is important that we first
setup the environment to be unique to you, otherwise we will get
naming conflicts between people running the tutorials. 

You can do this through interactive variables here, or you can set
values in a local `env.local.json` file. We recommend that you start
by copying the existing `env.json` file.

The currently defined variables are:

```
env | grep ACS_*
```

If you are running in interactive mode simply continue and you will be
prompted for any mising values when necessary.

# Prerequisites

You will need an active azure subscriptio and you will need to have
the Azure CLI installed and you'll need to
be [logged in to Azure](../../Azure/login/README.md).

## Creating the Cluster

Now that we are logged in we can get to work. 

We will use the Azure CLI 2.0 to quickly create an Azure Container
Services cluster.

First, we will create a resource group for the ACS cluster to be
deployed. You could use one already available, but here we will create
a new one as it makes it easy to clean up after the demo.

```
az group create --name $ACS_RESOURCE_GROUP --location $ACS_REGION
```

Results: 

```Expected_similarity=0.4
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/my_resource_group",
  "location": "eastus",
  "managedBy": null,
  "name": "my_resource_group",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null
}
```

Finally we create the cluster. In this example we will let the CLI use
default settings and allow it to discover and/or create the necessary
credentials. We can specific them in the command if we want to (see
`az acs create --help` for more details). This process typicaly takes
15-20 minutes.

```
az acs create --orchestrator-type=kubernetes --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```

Results:

```expected_similarity=0.2
waiting for AAD role to propagate.done 
{ 
  "id": "/subscriptions/325e7c34-99fb-4190-aa87-1df746c67705/resourceGroups/k8sdemoVWOAMOBQ/providers/Microsoft.Resources/deployments/azurecli1494143933.8352275",
  "name": "azurecli1494143933.8352275", 
  "properties": { 
    "correlationId": "e921ef72-f6ae-4e86-a2c3-7f3cb1001e59", 
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
	"providers": [ { 
	  "id": null, "namespace": "Microsoft.ContainerService", 
	  "registrationState": null,
      "resourceTypes": [ 
	    { 
		  "aliases": null, 
		  "apiVersions": null,
          "locations": [ "eastus" ], 
		  "properties": null, 
		  "resourceType": "containerServices" 
	    } 
	  ] 
	} ], 
  "provisioningState": "Succeeded",
  "template": null, 
  "templateLink": null, 
  "timestamp": "2017-05-07T08:03:59.208194+00:00" 
  }, 
  "resourceGroup": "k8sdemoVWOAMOBQ" 
}
```

# Wait for Cluster to become available

At the time of writing there is
a [bug in ACS](https://github.com/Azure/ACS/issues/36) that results in
the cluster reporting it is created but it taking a few minutes longer
before it is actually available.

```
sleep 180
```

# Validation

Check the cluster has been created:

```
az acs wait --resource-group $ACS_RESOURCE_GROUP --name $ACS_CLUSTER_NAME --created --timeout 15
```

If the cluster has been successfully created this command returns nothing.

Results:

```
```


