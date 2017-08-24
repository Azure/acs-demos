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

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll need to
be [logged in to Azure](../../azure/login/README.md).

You will need to have create
a
[resource group](../../incubator/azure_compute/resource_group/create/)
in which to place your ACS cluster.

## Creating the Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container
Services cluster.

In this example we will let the CLI use default settings and allow it
to discover and/or create the necessary credentials. We can specific
them in the command if we want to (see `az acs create --help` for more
details). This process typicaly takes 15-20 minutes.

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

Check the cluster has been created. If the cluster has been successfully created this command returns nothing.

```
az acs show --resource-group $ACS_RESOURCE_GROUP --name $ACS_CLUSTER_NAME
```

Results:

```
Location        Name          ProvisioningState    ResourceGroup
--------------  ------------  -------------------  ---------------
southcentralus  acs-k8s-test  Succeeded            acs-k8s-test
```


