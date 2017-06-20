# Create Docker CE cluster on Azure

In this demo we'll use the Azure CLI to create a Docker CE
installation on Azure.

## Create the resource group

```
az group create --name $ACS_RESOURCE_GROUP_NAME --location $ACS_RESOURCE_GROUP_LOCATION
```

## Create the ACS cluster

At the time of writing thre is no `az acs create` command in the CLI,
we will therefore create the cluster using the more complex `az group
deployment create` command.

```
az group deployment create --resource-group $ACS_RESOURCE_GROUP_NAME --template-file azuredeploy.json --parameters '{"dnsNamePrefix" : { "value" : "'"$ACS_DNS_PREFIX"'" }, "sshRSAPublicKey" : { "value" : "'"$ACS_SSH_PUBLIC_KEY"'" }}'
```

## Connect to the cluster

To manage this cluster we will create an SSH tunnel to the masters:

```
ssh -fNL 2375:localhost:2375 -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.$ACS_RESOURCE_GROUP_LOCATION.cloudapp.azure.com
```

We also need to ensure the Docker CLI is communicating over this tunnel, in our case we have already set the environment variable:

```
DOCKER_HOST=:2375
```

Lets check we are connected:

```
docker info
```
