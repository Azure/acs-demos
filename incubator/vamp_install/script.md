# What is Vamp?

Vamp is an open source solution that provides canary releasing and
autoscaling for microservices. It runs on Kubernetes, DC/OS and Docker
clusters. In this tutorial/demo we will focus on installing Vamp on
ACS with DC/OS.

# Create a Cluster

First we need an ACS cluster. Other documents go into detail about how
to build a cluster so here we'll keep the details to a minimum. For
the purposes of this document we assume that you already have
the
[Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) installed.

First, login to your Azure subscription.

```
az login
```

Now we need a resource group in which our ACS cluster will be deployed.

```
az group create -n rgdcosvamp -l eastus2
```

Finally we will create the cluster:

```
az acs create -n rgdcosvamp -g rgdcosvamp -d rgdcosvamp --generate-ssh-keys
```

## Install DC/OS CLI

In order to manage this instance of ACS we will need the DC/OS cli,
fortunately the Azure CLI makes it easy to install it.

```
sudo az acs dcos install-cli
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel:

```
sudo ssh -fNL 80:localhost:80 -p 2200 azureuser@rgdcosvampmgmt.eastus2.cloudapp.azure.com -i ~/.ssh/id_rsa
```

Now we tell the DC/OS CLI to use this tunnel to communicate with the cluster.

```
dcos config set core.dcos_url http://localhost
```

# Install Vamp on the cluster

## Deploy Elastic Search

Using marathon application definition file `elasticsearch.json` we
will deploy Elastic Search to our cluster. Lets take a look at that
file. We should note that this configuration of Elastic Search is not
intended for production deployments, it's a quick and simple
installation for this demo/tutorial.

```
cat elasticsearch.json
```

To deploy the application we can use the web UI, but here we will use
the CLI:

```
dcos marathon app add elasticsearch.json
```

# Deploy Vamp

Finally we will deploy Vamp using DC/OS Universe, we need to configure
the Elastic Search URL by providing a configuration file.

```
cat vamp.json
```

With this file the application itself is deployed using the DC/OS cli.

```
dcos package install vamp --options vamp.json --yes
```

We can connect to the service using the SSH tunnel we created earlier.

http://localhost/service/vamp
