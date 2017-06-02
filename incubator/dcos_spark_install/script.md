# What is Spark?

[Spark](https://spark.apache.org/) is a powerful general cluster computing system for Big Data. We will be using [DC/OS](https://dcos.io/) to deploy a Spark cluster. We will also install [Zeppelin](https://zeppelin.apache.org/), a web-based notebook for data analytics, making it easier to interact with Spark.

# Creating a Cluster

We will use the Azure CLI 2.0 to quickly create an Azure Container Services cluster. Ensure you have the Azure CLI installed and have logged in.

```
az login
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
sudo az acs dcos install-cli
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel, allowing us to view the DC/OS UI on our local machine.

```
sudo ssh -fNL 80:localhost:80 -p 2200 azureuser@acs-dcos-spark-dnsmgmt.eastus.cloudapp.azure.com -i ~/.ssh/id_rsa
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

*Note: you need to have virtualenv set up to install the Spark package (`sudo pip install virtualenv`).*

Once Spark is deployed, it will be available at [http://localhost/service/spark/](http://localhost/service/spark/).

Next, we can deploy Zeppelin.

```
dcos install zeppelin
```

Once deployed, Zeppelin will be available at [http://localhost/service/spark/](http://localhost/service/spark/)