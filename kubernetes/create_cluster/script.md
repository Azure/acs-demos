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

## Azure Login

You will need an active azure subscription. Before proceeding with
this script ensure that you are logged in using `az login`.

```
az account show --output=table
```

Results:

```expected_similarity=0.4
EnvironmentName    IsDefault    Name                           State    TenantId
-----------------  -----------  -----------------------------  -------  ------------------------------------
AzureCloud         True         Azure Container Service Demos  Enabled  72f988bf-86f1-41af-91ab-2d7cd011db47
```

# Ensuring we have a clean cluster

It's always wise to ensure that a demo starts in a clean state. To
that end we will delete any existing cluster and SSH infromation that
exists using this configuration. Don't worry if this command returns a
"could not be found" error. It just means you didn't have anything
dangling after the last demo.

```
az group delete --name $ACS_RESOURCE_GROUP --yes
```

## Creating a Cluster

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

Finally we create the cluster. In this example we will let the CLI use
default settings and allow it to discover and/or create the necessary
credentials. We can specific them in the command if we want to (see
`az acs create --help` for more details). This process takes 5-10
minutes.

```
az acs create --orchestrator-type=kubernetes --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```

Results:

```expected_similarity=0.4
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
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
```
               
We can now interact with the cluster, for example we can get a list of
nodes.

```
kubectl get nodes
```

Results:

```expected_similarity=0.4
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      1m
k8s-agent-14de76a8-1    Ready                      1m
k8s-agent-14de76a8-2    Ready                      1m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   1m
```


