# Ensure we are connected to the DC/OS cluster

Here we will prepare a demo environment for managing DC/OS workloads.

# Prerequisites

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll need to
be [logged in to Azure](../../azure/login/README.md).

You will first need to ensure you have
a [working DC/OS cluster](../create_cluster/script.md).

We'll be using SSH, so lets ensure it is installed:

```
sudo apt-get install openssh-client -y
```

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${SIMDEM_DNS_PREFIX}mgmt.${SIMDEM_LOCATION}.cloudapp.azure.com &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

## Install VirtualEnv

Many DC/OS tools use VitualEnv, so let's add that:

```
sudo pip3 install virtualenv
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

## Validation

We can check we are connected to the cluster with the DC/OS CLI, for
exampel:

```
dcos node
```

Result:

```
FIXME
```
