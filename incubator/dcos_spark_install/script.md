# What is Spark?

Spark is a powerful general cluster computing system for Big Data. We will be using DC/OS to deploy a Spark cluster.

# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container Services cluster. Ensure you have the Azure CLI installed and have logged in.

```
az login
```

Next, we will create a resource group for the ACS cluster to be deployed.

```
az group create -n $CLUSTER_GROUP_NAME -l $CLUSTER_LOCATION
```

Now, we can create the cluster

```
az acs create -n $CLUSTER_NAME -g $CLUSTER_GROUP_NAME -d $CLUSTER_DNS_PREFIX --generate-ssh-keys
```

## Install the DC/OS CLI

In order to manage this instance of ACS we will need the DC/OS cli,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs dcos install-cli
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel:

```
sudo ssh -fNL 80:localhost:80 -p 2200 azureuser@$CLUSTER_DNS_PREFIXagent.eastus.cloudapp.azure.com -i ~/.ssh/id_rsa
```

Now we tell the DC/OS CLI to use this tunnel to communicate with the cluster.

```
dcos config set core.dcos_url http://localhost
```

At this point, the DC/OS interface should be available at [https://localhost](https://localhost).

# Deploying Spark

We can use the DC/OS cli to set up Spark.

```
dcos package install spark
```

Next, we can deploy Zeplin.

```
dcos install zeplin
```