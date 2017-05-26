In order to create a kubernetes cluster in Azure Container Service we
will first ensure that we have the Azure CLI v2 installed.

```
az --version
```

Results:

```
azure-cli (2.0.1)

acs (2.0.1)
appservice (0.1.1b6)
batch (0.1.1b5)
cloud (2.0.0)
component (2.0.0)
configure (2.0.1)
container (0.1.1b4)
core (2.0.1)
documentdb (0.1.1b2)
feedback (2.0.0)
find (0.0.1b1)
iot (0.1.1b3)
keyvault (0.1.1b6)
network (2.0.1)
nspkg (2.0.0)
profile (2.0.1)
redis (0.1.1b3)
resource (2.0.1)
role (2.0.0)
sql (0.1.1b6)
storage (2.0.1)
vm (2.0.1)

Python (Linux) 2.7.12 (default, Nov 19 2016, 06:48:10)
[GCC 5.4.0 20160609]
```

If you do not yet have it installed you can install it from
https://docs.microsoft.com/en-us/cli/azure/install-azure-cli.

Next you need to verify that you have logged in and the correct
account is active.

```
az login
az account show --output=table
```

Results:

```
EnvironmentName    IsDefault    Name                           State    TenantId
-----------------  -----------  -----------------------------  -------  ------------------------------------
AzureCloud         True         Azure Container Service Demos  Enabled  72f988bf-86f1-41af-91ab-2d7cd011db47
```

Now that we are logged in we can get to work. 

The first thing we need to do is create a resource group. To prevent
name clashes I'm going to generate a hash for my cluster.

```
ID=$(cat /dev/urandom | tr -dc 'A-Z0-9' | fold -w 8 | head -n 1)
echo $ID
```

Results:

```
2FPSW8O9
```

Now we will generate various names and values needed for our
cluster. Here we will use the same name for a number of the resources,
in production you will likely use different names.

```
DNS_PREFIX="k8sdemo$ID"
CLUSTER_NAME=$DNS_PREFIX
RESOURCE_GROUP=$DNS_PREFIX
LOCATION=eastus
```

Lets create the resource group for our cluster. You could use one
already available, but here we will create a new one as it makes it
easy to clean up after the demo - simply delete the resource group.

```
az group create --location=$LOCATION --name=$RESOURCE_GROUP
```

Results:

```
{
  "id": "/subscriptions/325e7c34-99fb-4190-aa87-1df746c67705/resourceGroups/k8sdemoVWOAMOBQ",
    "location": "eastus",
    "managedBy": null,
    "name": "k8sdemoVWOAMOBQ",
    "properties": {
      "provisioningState": "Succeeded"
    },
    "tags": null
}
```

Finally we create the cluster. In this example we will let the CLI use
default settings and allow it to discover and/or create the necessary
credentials. We can specific them in the command if we want to (see
`az acs create --help` for more details. This process takes 5-10
minutes.

```
az acs create --orchestrator-type=kubernetes --resource-group=$RESOURCE_GROUP --name=$CLUSTER_NAME
```

Results:

``` 
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

The cluster is now created, but we need to get the credentials from it
so that the Kubernetes CLI can operate on it:

```
az acs kubernetes get-credentials --resource-group=$RESOURCE_GROUP --name=$CLUSTER_NAME
```

We are almost ready to inspect the cluster, but first we need to
install the kubectl CLI.

```
az acs kubernetes install-cli
```

Finally we can verify if the cluster looks as expected (the defaults
are 1 master and 3 nodes:

```
kubectl get nodes
```

Results:

```
NAME                    STATUS                     AGE
k8s-agent-14de76a8-0    Ready                      1m
k8s-agent-14de76a8-1    Ready                      1m
k8s-agent-14de76a8-2    Ready                      1m
k8s-master-14de76a8-0   Ready,SchedulingDisabled   1m
```

## Next Steps

If you already know how to use Kubernetes then you are now good to
go. Just work with Kuberenets on ACS just as you would any other
installation. If you are new to Kubernetes you may want to run through
our "Hello World" tutorial.

FIXME: Write the Hello World tutorial
