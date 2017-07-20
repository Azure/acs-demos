# Azure Login

You will need an active azure subscription. Before proceeding with
this script ensure that you are logged in using `az login`. 

# Environment Setup

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

# Dependencies

There are a few dependencies that we must have installed for this
tutorial/demo to work:

```
sudo pip3 install virtualenv
sudo apt-get install openssh-client -y
```

# Ensuring we have a clean cluster

It's always wise to ensure that a demo starts in a clean state. To
that end we will delete any existing cluster and SSH infromation that
exists using this configuration. Don't worry if this command returns a
"could not be found" error. It just means you didn't have anything
dangling after the last demo.

```
az group delete --name $ACS_RESOURCE_GROUP --yes
sudo ssh-keygen -f "~/.ssh/known_hosts" -R [${ACS_DNS_PREFIX}mgmt.$ACS_REGION.cloudapp.azure.com]:2200
```

# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container
Services cluster. Ensure you have the Azure CLI installed and have
logged in using `az login`. 

First, we will create a resource group for the ACS cluster to be
deployed.

```
az group create --name $ACS_RESOURCE_GROUP --location $ACS_REGION
```

Results: 

```Expected_results=0.4
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

Now, we can create the cluster.

```
az acs create --name $ACS_CLUSTER_NAME --resource-group $ACS_RESOURCE_GROUP --dns-prefix $ACS_DNS_PREFIX --agent-count 6 --generate-ssh-keys
```

Results:

```Expected_results=0.05
{
  "id": "/subscriptions/135f79ed-bb93-4372-91f6-7b5f1c57dd81/resourceGroups/acs-dcos-spark-demo/providers/Microsoft.Resources/deployments/azurecli1496363170.3581209",
  "name": "azurecli1496363170.3581209",
  "properties": {
    "correlationId": "d9ac5c3f-83ec-4d30-861f-5c331f8ac40b",
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
    "providers": [
      {
        "id": null,
        "namespace": "Microsoft.ContainerService",
        "registrationState": null,
        "resourceTypes": [
          {
            "aliases": null,
            "apiVersions": null,
            "locations": [
              "eastus"
            ],
            "properties": null,
            "resourceType": "containerServices"
          }
        ]
      }
    ],
    "provisioningState": "Succeeded",
    "template": null,
    "templateLink": null,
    "timestamp": "2017-06-02T00:32:36.607132+00:00"
  },
  "resourceGroup": "acs-dcos-spark-demo"
}
```

## Install the DC/OS CLI

In order to manage this instance of ACS we will need the DC/OS cli,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs dcos install-cli
```

Results:

```
Downloading client to /usr/local/bin/dcos
```

Now we tell the DC/OS CLI to use localhost as the DCOS URL. When we
want to connect to the cluster we will create an SSH tunnel between
localhost (port 10000) and the cluster.

```
dcos config set core.dcos_url http://localhost:10000
```

Results:

```
[core.dcos_url]: changed from 'http://localhost:80' to 'http://localhost:10000'
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com -i ~/.ssh/id_rsa &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

At this point, the DC/OS interface should be available
at [https://localhost:10000](https://localhost:10000) and your DC/OS CLI will be
able to communicate with the cluster:

```
dcos node
```

Results:  Expected results: 0.1

```
HOSTNAME      IP                        ID
 10.0.0.4   10.0.0.4  21638e0a-f223-4598-a73c-1e991fe2c069-S2
10.32.0.4  10.32.0.4  21638e0a-f223-4598-a73c-1e991fe2c069-S3
10.32.0.6  10.32.0.6  21638e0a-f223-4598-a73c-1e991fe2c069-S1
10.32.0.7  10.32.0.7  21638e0a-f223-4598-a73c-1e991fe2c069-S0
```

Assuming you can see the node list above you now have your demo
environment set up.

# Next Steps

  1. [Install Apache Spark and Apache Zeppelin](../install/script.md)
  2. [Delete the ACS DC/OS cluster](../cleanup/script.md)
