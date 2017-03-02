# Deploy Docker Swarm Mode cluster using ACS Engine

## Introduction

In this lab you will learn:

- How to use ACS Engine to deploy a Docker Swarm mode cluster on Microsoft Azure
- How to connect to the cluster

## Set up your machine

ACS Engine is an open source tool written in Go that you can use to create Docker-enable cluster on Microsoft Azure.

All the steps to get started with ACS Engine on your machine are described [here](https://github.com/Azure/acs-engine/blob/master/docs/acsengine.md).

In short you need to:

1. Download and install Git
2. Download and install Go
3. Set up your Go workspace
4. Build ACS Engine

You also need to install the *Azure CLI 2.0* on your machine, following [this documentation](https://docs.microsoft.com/en-us/cli/azure/install-az-cli2).

## Create an SSH Key

An SSH key will be required to create the cluster. You will also use it late to connect the master node using SSH.

Depending the OS your machine are running on, you can use one of these documentation to get an ssh key:

- [Linux, MacOS or Windows 10 with Ubuntu Bash](https://docs.microsoft.com/en-us/azure/virtual-machines/virtual-machines-linux-ssh-from-windows?toc=%2fazure%2fvirtual-machines%2flinux%2ftoc.json)
- [Windows](https://docs.microsoft.com/en-us/azure/virtual-machines/virtual-machines-linux-ssh-from-windows?toc=%2fazure%2fvirtual-machines%2flinux%2ftoc.json)

## Create the cluster definition file

ACS Engine is really easy to use. The only required parameter is a JSON file that describes the cluster you want to create.

For Swarm Mode, you can start with the following:

```json
{
  "apiVersion": "vlabs",
  "properties": {
    "orchestratorProfile": {
      "orchestratorType": "DockerCE"
    },
    "masterProfile": {
      "count": 1,
      "dnsPrefix": "DNS_PREFIX_FOR_MASTERS",
      "vmSize": "Standard_D2_v2"
    },
    "agentPoolProfiles": [
      {
        "name": "agentpool1",
        "count": 2,
        "vmSize": "Standard_D2_v2",
        "dnsPrefix": "DNS_PREFIX_FOR_AGENT_POOL",
        "ports": [
          80,
          443,
          8080
        ]
      }
    ],
    "linuxProfile": {
      "adminUsername": "azureuser",
      "ssh": {
        "publicKeys": [
          {
            "keyData": "YOUR_SSH_PUBLIC_KEY"
          }
        ]
      }
    }
  }
}
```

1. Copy this JSON payload and save it into a **swarmmode.json** file.
2. Fill the **dnsPrefix** property for the master and agent profiles.
3. Set the **keyData** property with the value of the SSH public key that you have generated.

## Generate the Azure Resource Manager (ARM) template

Now that your cluster definition file is ready, you should use ACS Engine to get the ARM template and the template parameter file that you will use to deploy the Swarm cluster:

```
acs-engine.exe swarmmode.json
```

This command will generates 3 files:

- **apimodel.json**: a copy of the cluster definition file you gave in input
- **azuredeploy.json**: the ARM template
- **azuredeploy.json**: the ARM template parameters file

You are going to use these file to deploy the cluster.

## Deploy the Docker Swarm cluster to Microsoft Azure

1. Log in to your Microsoft Azure account using the **az login** command:

```
az login
```

You will be asked to go to https://aka.ms/devicelogin, enter the code and authenticate with your Microsoft Azure credentials.

2. Select the account you want to use:

```
az account select --subscription SUBSCRIPTION_ID
```

3. Create a new resource group:

```
az group create \
    --name "RESOURCE_GROUP_NAME" \
    --location "LOCATION"
```

4. Create a new deployment in the resource group, using the ARM template you have generated with ACS Engine:

```
az group deployment create \
    --name "DEPLOYMENT NAME" \
    --resource-group "RESOURCE_GROUP_NAME" \
    --template-file "./_output/INSTANCE_ID/azuredeploy.json" \
    --parameters "@./_output/INSTANCE_ID/azuredeploy.parameters.json"
```

Wait for the deployment to be completed. Depending on the number of virtual machines you have asked for in the cluster definition file, it can take a while.

## Connect to your new Docker Swarm mode cluster

Once the deployment is completed successfuly, it's time to connect to the cluster and check that all is working great. To work with a Docker Swarm mode cluster on Microsoft Azure, we recommand that you create an SSH tunnel between your machine and one of the masters and that you use port forwarding to work with the Swarm endpoint through this tunnel.

Depending on if you are running Linux, Windows or macOS the way to create the SSH tunnel to the master will differ, but the procedure is documented on [this page](https://docs.microsoft.com/en-us/azure/container-service/container-service-connect#connect-to-a-dcos-or-swarm-cluster), for each OS.

Once the SSH tunnel created, open a CLI and set the **DOCKER_HOST** environment variable to the port you have forwarded on your machine:

*Linux or macOS*

```
export DOCKER_HOST=:2375
```

*Windows*

```
set DOCKER_HOST=:2375
```

And test that the connection is working using the *docker info* command:

```
C:\Users\jucoriol>set DOCKER_HOST=:2375

C:\Users\jucoriol>docker info
Containers: 0
 Running: 0
 Paused: 0
 Stopped: 0
Images: 0
Server Version: 1.13.1
Storage Driver: aufs
 Root Dir: /var/lib/docker/aufs
 Backing Filesystem: extfs
 Dirs: 0
 Dirperm1 Supported: true
Logging Driver: json-file
Cgroup Driver: cgroupfs
Plugins:
 Volume: local
 Network: bridge host macvlan null overlay
Swarm: active
 NodeID: 0zp4u4mbj8n5ro7c42gihyavo
 Is Manager: true
 ClusterID: y2uut5z6gwc23byqvbnojb6gq
 Managers: 1
 Nodes: 3
 Orchestration:
  Task History Retention Limit: 5
 Raft:
  Snapshot Interval: 10000
  Number of Old Snapshots to Retain: 0
  Heartbeat Tick: 1
  Election Tick: 3
 Dispatcher:
  Heartbeat Period: 5 seconds
 CA Configuration:
  Expiry Duration: 3 months
 Node Address: 172.16.0.5
 Manager Addresses:
  172.16.0.5:2377
Runtimes: runc
Default Runtime: runc
Init Binary: docker-init
containerd version: aa8187dbd3b7ad67d8e5e3a15115d3eef43a7ed1
runc version: 9df8b306d01f59d3a8029be411de015b7304dd8f
init version: 949e6fa
Security Options:
 apparmor
 seccomp
  Profile: default
Kernel Version: 4.4.0-47-generic
Operating System: Ubuntu 16.04.1 LTS
OSType: linux
Architecture: x86_64
CPUs: 2
Total Memory: 6.804 GiB
Name: swarmm-master-10319951-0
ID: MKNG:QNIP:KVWI:2C3F:A7ZG:JPJ3:OLV6:I5XL:SU3T:24LD:MYL5:DRXN
Docker Root Dir: /var/lib/docker
Debug Mode (client): false
Debug Mode (server): false
Registry: https://index.docker.io/v1/
WARNING: No swap limit support
Experimental: false
Insecure Registries:
 127.0.0.0/8
Live Restore Enabled: false
```

*Note: if you have Docker (or Docker for Windows, or Docker for Mac) running on your machine, forwarding the port to 2375 may not work. In that case, just forward it to another one, like 2376 for example.*

You are now done with the deployment of a Docker Swarm mode cluster on Microsoft Azure using ACS Engine.