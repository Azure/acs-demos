# Download and install ACS Engine

In this document we'll prepare your environment for running the
tutorials and demos we have for ACS Engine.

# Prerequisite

You will need to have create
a
[resource group](../../incubator/azure_compute/resource_group/create/)
in which to place your ACS cluster.

# Setup the client

This section focuses on ensuring your client machine is ready. Here we
are using a Linux environment.

## Create Workspace

We need a workspace for storing things like generated templates.

```
mkdir -p $SIMDEM_ACSE_WORKSPACE
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
wget -O $SIMDEM_ACSE_WORKSPACE/acs-engine.tar.gz https://github.com/Azure/acs-engine/releases/download/v$SIMDEM_ACSE_VERSION/acs-engine-v$SIMDEM_ACSE_VERSION-linux-amd64.tar.gz
```

and extract it:

```
tar -C $SIMDEM_ACSE_WORKSPACE -zxvf $SIMDEM_ACSE_WORKSPACE/acs-engine.tar.gz
```

Copy the executable into `/usr/local/bin`

```
sudo cp $SIMDEM_ACSE_WORKSPACE/linux-amd64/acs-engine /usr/local/bin/acs-engine
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
if [ ! -f "$SIMDEM_ACSE_SSH_KEY" ]; then ssh-keygen -t rsa -N "" -f $SIMDEM_ACSE_SSH_KEY; fi
```



