# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container
Services cluster. Ensure you have the Azure CLI installed and have
logged in using the `az login` command.

We first need to set a few environment variables we will use later:

```
RESOURCE_GROUP=acs-spark-demo
SERVICE_NAME=acs-dcos-spark
DNS_NAME_PREFIX=acs-dcos-spark
REGION=eastus
```

```
echo region is $REGION
```

Next, we will create a resource group for the ACS cluster to be deployed.

```
az group create -n acs-dcos-spark-demo -l eastus
```

Now, we can create the cluster.

```
az acs create -n acs-dcos-spark-cluster -g acs-dcos-spark-demo -d acs-dcos-spark-dns --generate-ssh-keys
```

## Install the DC/OS CLI

In order to manage this instance of ACS we will need the DC/OS cli,
fortunately the Azure CLI makes it easy to install it.

```
pip install virtualenv
az acs dcos install-cli
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel:

```
ssh -fNL 80:localhost:80 -p 2200 azureuser@acs-dcos-spark-dnsmgmt.eastus.cloudapp.azure.com -i ~/.ssh/id_rsa
```

Now we tell the DC/OS CLI to use this tunnel to communicate with the cluster.

```
dcos config set core.dcos_url http://localhost
```

