# Create an Azure Container Instance

Here we will create a single instance of a container using Azure Container Registry.

## Prerequisites

This demo uses a few common [Linux commands](./dependencies/README.md) that we
must ensure are installed.

You must have installed the [Azure CLI](../../azure_compute/cli/install/).

You will need to have created
a [resource group](../../azure_compute/resource_group/create) in
which to run your container instance.

## Create an ACI Container

It's trivial to create an ACI container within a given resource group
using the Azure CLI:

```
az container create --name $SIMDEM_ACI_INSTANCE_NAME --image $SIMDEM_ACI_CONTAINER --resource-group $SIMDEM_RESOURCE_GROUP --ip-address public
```

Results:

```expected_similarity=0.25
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

## Connect to the Container Endpoint

In order to connect to this instance we will need its assigned IP
number. The following will put it into a variable for us.

```
IP_NUMBER=$(az container show --resource-group $SIMDEM_RESOURCE_GROUP --name $SIMDEM_ACI_INSTANCE_NAME --output tsv --query ipAddress.ip)
```

So what's the IP number?

```
echo $IP_NUMBER
```

Now we can check our web service is alive, we'll give it up to 90 seconds to respond, but usually it is much faster than this:

```
xdg-open  $IP_NUMBER
```

Results:

```
HTTP/1.1 200 OK
X-Powered-By: Express
Accept-Ranges: bytes
Cache-Control: public, max-age=0
Last-Modified: Thu, 20 Jul 2017 21:57:04 GMT
ETag: W/"67f-15d62011380"
Content-Type: text/html; charset=UTF-8
Content-Length: 1663
Date: Mon, 31 Jul 2017 23:23:33 GMT
Connection: keep-alive
```

## View Container Logs

We can also view the logs of the container:

```
az container logs --resource-group $SIMDEM_RESOURCE_GROUP --name $SIMDEM_ACI_INSTANCE_NAME
```

# Next Steps

  1. [Cleanup resources used in this demo](cleanup/script.md)
  2. [ACI demo index](../README.md)
