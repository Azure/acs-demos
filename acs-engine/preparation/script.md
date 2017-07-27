# Download and install ACS Engine

In this document we'll prepare your environment for running the
tutorials and demos we have for ACS Engine.

# Setup the client

This section focuses on ensuring your client machine is ready. Here we
are using a Linux environment.

## Environment Variables

The demo/tutorial scripts use environment variables to ensure that
they all work well together. The following environment variables are
currently used:

```
env | grep ACSE.*
```

Results:

```
ACSE_WORKSPACE=~/.acs-engine/simdem
ACSE_DNS_PREFIX=rg-acse-demo
ACSE_LOCATION=eastus
ACSE_RESOURCE_GROUP=acse-demo
ACSE_SERVICE_PRINCIPLE_NAME=acs-engine-simdem
ACSE_SERVICE_PRINCIPLE_PASSWORD=pa$$w0rd
ACSE_VERSION=0.4.0
ACSE_SUBSCRIPTION_ID=135fxxxx-xxxx-xxxx-xxxx-7b5f1c57xxxx
ACSE_SSH_KEY=/home/rgardler/.ssh/id_rsa
```

## Create Workspace

We need a workspace for storing things like generated templates.

```
mkdir -p $ACSE_WORKSPACE
```

## Required software

These demos and tutorials use a few tools to ease the way, these are
not really required to use ACS Engine, but we find them useful here:

```
sudo apt-get update
sudo apt-get install wget -y
sudo apt-get openssh-client -y
sudo apt-get install jq -y
```

## Download ACS Engine

The ACS Engine project releases binaries via Github. We'll download
it:

```
wget -O $ACSE_WORKSPACE/acs-engine.tar.gz https://github.com/Azure/acs-engine/releases/download/v$ACSE_VERSION/acs-engine-v$ACSE_VERSION-linux-amd64.tar.gz
```

and extract it:

```
tar -C $ACSE_WORKSPACE -zxvf $ACSE_WORKSPACE/acs-engine.tar.gz
```

Copy the executable into `/usr/local/bin`

```
sudo cp $ACSE_WORKSPACE/linux-amd64/acs-engine /usr/local/bin/acs-engine
```

Lets check it's installed:

```
acs-engine --help
```

Results:

```
ACS-Engine deploys and manages Kubernetes, Swarm Mode, and DC/OS clusters in Azure

Usage:
  acs-engine [command]
  
Available Commands:
  deploy      deploy an Azure Resource Manager template
  generate    Generate an Azure Resource Manager template
  help        Help about any command
  version     Print the version of ACS-Engine
		  
Flags:
	          --debug   enable verbose debug logs
	      -h, --help    help for acs-engine
				  
Use "acs-engine [command] --help" for more information about a command.
```

## Ensure we have a valid SSH key pair

Kubernetes clusters use SSH for communication with the masters. The
followin line will check there is a valid SSH key available and, if
not, create one.

```
if [ ! -f "$ACSE_SSH_KEY" ]; then ssh-keygen -t rsa -N "" -f $ACSE_SSH_KEY; fi
```

# Setup Azure Environment

Now the client is ready we need to ensure that we have an Azure
environment to work in. You must first install
the
[Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) and
log in to your account using `azure login`.

## Ensure we are using the right Azure Subscription

In case you have multiple subscriptions we need to ensure we are using
the right one.

```
az account set --subscription $ACSE_SUBSCRIPTION_ID
```

## Create a resource group

All the demo's and tutorials will operate in a single resource group,
so lets get it ready.

```
az group create --name $ACSE_RESOURCE_GROUP --location $ACSE_LOCATION
```

## Ensure we have a valid service principle

A service principle allows Kuberntes to manage the Azure
infrastrcuture for you, so lets create one now.

```
az ad sp create-for-rbac --name $ACSE_SERVICE_PRINCIPLE_NAME --role="Contributor" --scopes="/subscriptions/$ACSE_SUBSCRIPTION_ID/resourceGroups/$ACSE_RESOURCE_GROUP" --password $ACSE_SERVICE_PRINCIPLE_PASSWORD
```

Results:

```expected_similarity=0.3
Retrying role assignment creation: 1/36
{
  "appId": "657cd27f-xxxx-xxxx-xxxx-512adb26e468",
  "displayName": "acs-engine-simdem",
  "name": "http://acs-engine-simdem",
  "password": "51d6a411-xxxx-xxxx-xxxx-974053b1a80a",
  "tenant": "72f988bf-xxxx-xxxx-xxxx-2d7cd011db47"
}
```




