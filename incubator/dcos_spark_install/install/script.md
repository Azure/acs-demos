# What we will do

We will:

  * Install Apache Spark on an ACS DC/OS cluster
  * Install Apache Zeppelin on an ACS DC/OS cluster

# Preparation

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it
now. This will have pre-created a cluster with the following
configuration:

```
env | grep ACS_.*
```

## Setup SSH Tunnel

We first need to ensure that we can connect to the DC/OS masters by
opening an SSH tunnel:

```
sudo apt-get install openssh-client -y
sudo ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com -i ~/.ssh/id_rsa &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

## Install DC/OS CLI

We'll be using both the web interface and the CLI, so lets install the
CLI:

```
sudo az acs dcos install-cli
dcos config set core.dcos_url http://localhost:10000
```

## Install VirtualEnv

The Spark CLI uses VitualEnv, so let's add that:

```
sudo pip3 install virtualenv
```

# Interacting with DC/OS

At this point, the DC/OS web interface should be available
at [https://localhost:10000](https://localhost:10000) and your DC/OS
CLI will be able to communicate with the cluster:

```
xdg-open http://localhost:10000
xdg-open http://localhost:10000/#/nodes
xdg-open http://localhost:10000/#/services/overview
xdg-open http://localhost:10000/#/universe/packages
```

We can also use the CLI to do the same things:

```
dcos node
dcos service
dcos package search spark
```

## Deploying the Apache Spark service

Packages can be installed via the browser or the CLI. We'll use the
CLI to install Apache Spark.

```
dcos package install spark --yes
```

Results:

```
Installing Marathon app for package [spark] version [1.0.9-2.1.0-1]
Installing CLI subcommand for package [spark] version [1.0.9-2.1.0-1]
New command available: dcos spark
DC/OS Spark is being installed!

        Documentation: https://docs.mesosphere.com/service-docs/spark/
        Issues: https://docs.mesosphere.com/support/
```

Once Spark is deployed, it will be available
at [http://localhost:10000/service/spark/](http://localhost:10000/service/spark/).

# Deploying the Apache Zeppelin Service

Apache Zeppelin is a web based notebook which has good integration
with Spark, lets install that too.

```
dcos package install zeppelin --yes
```

Results:

```
This DC/OS Service is currently in preview. There may be bugs, incomplete features, incorrect documentation, or other discrepencies. Preview packages should never be used in production!
Installing Marathon app for package [zeppelin] version [0.7.0]

        Documentation: https://github.com/dcos/examples/tree/master/1.8/zeppelin/
	    Issues: https://dcos.io/community/				
```

Once deployed, Zeppelin will be available
at [http://localhost:10000/service/spark/](http://localhost:10000/service/spark/)

```
# Open the Zeppelin Service
# 
# Demostrate it is a working deployment connected to Spark
```

# Working with Spark

As part of the Spark installation DC/OS has installed the Spark CLI.

```
dcos spark 
```

Results:

```expected_similarity=0.6
Usage:
    dcos spark --help
    dcos spark --info
    dcos spark --version
    dcos spark --config-schema
    dcos spark run --help
    dcos spark run --submit-args=<spark-args>
                   [--docker-image=<docker-image>]
                   [--verbose]
    dcos spark status <submissionId> [--verbose]
    dcos spark log <submissionId>
                   [--follow]
                   [--lines_count=<lines_count>]
                   [--file=<file>]
    dcos spark kill <submissionId> [--verbose]
    dcos spark webui																																   
```

From now on it's "just spark" so why not try some of
the [examples](http://spark.apache.org/examples.html) that the Apacke
Spark community have provided.

## Spark Web UI

You can also access the Spark Web UI via the Services page of the
DC/OS UI:

```
xdg-open http://localhost:10000/#/services/overview
xdg-open http://localhost:10000/service/spark
```

# Working with Apache Zeppelin

Apache Zeppelin is also available via the DC/OS Services page:

```
xdg-open http://localhost:10000/#/services/overview 
xdg-open http://localhost:10000/service/zeppelin/#/
```

# Next Steps

From here you can experiment with Zeppelin notebooks, run Spark jobs
or explore the DC/OS Universe.
