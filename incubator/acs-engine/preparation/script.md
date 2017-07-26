# Download and install ACS Engine

The following environment variables are currently configured:

```
env | grep ACSE.*
```

# Ensuring we have the software we need

```
sudo apt-get update
sudo apt-get install wget
```

# Download

The ACS Engine project releases binaries via Github. We'll download
it:

```
mkdir -p ~/bin/acs-engine
pushd ~/bin/acs-engine
wget https://github.com/Azure/acs-engine/releases/download/v$ACSE_VERSION/acs-engine-v$ACSE_VERSION-linux-amd64.tar.gz
```

and extract it:

```
tar -zxvf acs-engine-v$ACSE_VERSION-linux-amd64.tar.gz
```

and add it to the PATH:

```
PATH=$PATH:~/bin/acs-engine/linux-amd64
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

Finally, we'll clean up after ourselves:

```
rm acs-engine-v$ACSE_VERSION-linux-amd64.tar.gz
popd
```
