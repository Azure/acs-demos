# Download and install ACS Engine

# Setup the environment

## Environment Variables

The following environment variables are currently configured:

```
env | grep ACSE.*
```

## Create Workspace

```
mkdir -p $ACSE_WORKSPACE
```

## Required software

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

```
if [ ! -f "$ACSE_SSH_KEY" ]; then ssh-keygen -t rsa -N "" -f $ACSE_SSH_KEY; fi
```

# Setup Azure Environment

## Ensure we are using the right Azure Subscription

```
az account set --subscription $ACSE_SUBSCRIPTION_ID
```

## Create a resource group

```
az group create --name $ACSE_RESOURCE_GROUP --location $ACSE_LOCATION
```

## Ensure we have a valid service principle

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




