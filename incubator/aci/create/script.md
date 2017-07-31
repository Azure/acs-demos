# Create an Azure Container Instance

Here we will create a single instance of a container using Azure Container Registry.

## Prerequisites

You must have installed the [Azure CLI](../../azure_compute/cli/install/).

You will need to have created
a [resource group](../../azure_compute/resource_group/create) in
which to run your container instance.

## Environment Setup

The current environment setup is:

```
env | grep SIMDEM_.*
```

## Create the Container

```
az container create --name $SIMDEM_ACI_INSTANCE_NAME --image $SIMDEM_ACI_CONTAINER --resource-group $SIMDEM_RESOURCE_GROUP --ip-address public
```

Results:

```expected_similarity=0.45
{
  "containers": [
    {
	  "command": null,
	  "environmentVariables": [],
	  "image": "microsoft/aci-helloworld",
	  "instanceView": null,
	  "name": "simdem-helloworld",
	  "ports": [
		  {
			  "port": 80
		  }
	  ],
	  "resources": {
		  "limits": null,
		  "requests": {
			  "cpu": 1.0,
			  "memoryInGb": 1.5
		  }
	  },
	  "volumeMounts": null
	}
  ],
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/simdem_rg/providers/Microsoft.ContainerInstance/containerGroups/simdem-helloworld",
  "imageRegistryCredentials": null,
  "ipAddress": {
	"ip": "52.168.17.106",
	"ports": [
		{
			"port": 80,
			"protocol": "TCP"
		}
	]
  },
  "location": "eastus",
  "name": "simdem-helloworld",
  "osType": "Linux",
  "provisioningState": "Creating",
  "resourceGroup": "simdem_rg",
  "restartPolicy": null,
  "state": null,
  "tags": null,
  "type": "Microsoft.ContainerInstance/containerGroups",
  "volumes": null
}
```

In order to connect to this instance we will need its assigned IP
number. The following will put it into a variable for us.

```
IP_NUMBER=$(az container show --resource-group $SIMDEM_RESOURCE_GROUP --name $SIMDEM_ACI_INSTANCE_NAME --output tsv --query ipAddress.ip)
```

So what's the IP number?

```
echo $IP_NUMBER
```

Now we can view what is there...

```
curl $IP_NUMBER
```
