# Install VAMP onto an ACS (DC/OS) cluster

We'll install Vamp on Azure Container Service with DC/OS.

# What is Vamp?

[Vamp](http://vamp.io) is an open source solution that provides canary releasing and
autoscaling for microservices. It runs on Kubernetes, DC/OS and Docker
clusters. In this tutorial/demo we will focus on installing Vamp on
ACS with [DC/OS](https://dcos.io/).

# Preparation

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it
now. This will have pre-created a cluster with the following
configuration:

```
env | grep ACS_.*
```

# Setup environment for Vamp demo

Here we will prepare a demo environment for running the DC/OS Spark
demo.

## Validate cluster

You will first need to ensure you have a working DC/OS cluster. If you need to create one see [tutorial / demo](../../create_cluster/script.md).

You can check that the cluster is available using the Azure CLI as
follows:

```
az acs show -g $ACS_RESOURCE_GROUP -n $ACS_CLUSTER_NAME --query provisioningState
```

Results:

```
"Succeeded"
```

If this says "Failed" you will need
to [cleanup](../delete_cluster/script.md) and try redeploying the
cluster. If it says "Provisioning" wait a little longer before
proceeding.

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
sudo apt-get install openssh-client -y
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

# Interacting with DC/OS

At this point, the DC/OS web interface should be available
at [https://localhost:10000](https://localhost:10000) and your DC/OS
CLI will be able to communicate with the cluster:

```
curl -i http://localhost:10000
curl -i http://localhost:10000/#/nodes
curl -i http://localhost:10000/#/services/overview
curl -i http://localhost:10000/#/universe/packages
```

We can also use the CLI to do the same things for example:

```
dcos node
```

Results:

```expected_similarity=0.05
HOSTNAME      IP                        ID
 10.0.0.4   10.0.0.4  21638e0a-f223-4598-a73c-1e991fe2c069-S2
10.32.0.4  10.32.0.4  21638e0a-f223-4598-a73c-1e991fe2c069-S3
10.32.0.6  10.32.0.6  21638e0a-f223-4598-a73c-1e991fe2c069-S1
10.32.0.7  10.32.0.7  21638e0a-f223-4598-a73c-1e991fe2c069-S0
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

*At the time of writing, Bash on Windows was unable to connect to forwarded localhost port. In this case, the DC/OS web interface can be used to start Elastic Search. Navigate to Services -> Run a Service -> JSON Configuration and paste the contents of `elasticsearch.json`. Use Cygwin or Mingw on Windows to run in an automated flow.*

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

*At the time of writing, Bash on Windows was unable to connect to forwarded localhost port. In this case, the DC/OS web interface can be used to start Vamp. Navigate to Universe -> Packages, search for Vamp -> Advanced Installation, and enter the Elastic Search URL in `vamp.json`. Use Cygwin or Mingw on Windows to run in an automated flow.*

We can connect to the service using the SSH tunnel we created earlier.

[http://localhost/service/vamp](http://localhost/service/vamp)
