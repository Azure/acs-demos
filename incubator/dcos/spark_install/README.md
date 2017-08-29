# What are we going to do?

We'll install Apache Spark and Apache Zeppelin on Azure Container
Service with DC/OS.

## What is Apache Spark?

[Apache Spark](https://spark.apache.org/) is a powerful general cluster
computing system for Big Data. We will be
using [DC/OS](https://dcos.io/) to deploy a Spark cluster. We will
also install [Zeppelin](https://zeppelin.apache.org/), a web-based
notebook for data analytics, making it easier to interact with Spark.

## What is Apache Zeppelin?

[Apache Zepellin](http://zeppelin.apache.org/) web-based notebook that
enables interactive data analytics. It has excellent integration with
Apache Spark.

# Prerequisites

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll need to
be [logged in to Azure](../../azure/login/README.md).

You will first need to ensure you have
a [working DC/OS cluster](../create_cluster/README.md).


## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
sudo apt-get install openssh-client -y
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${SIMDEM_DNS_PREFIX}mgmt.${SIMDEM_LOCATION}.cloudapp.azure.com &
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

# Deploying the Apache Spark service

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
This DC/OS Service is currently in preview.

Installing Marathon app for package [zeppelin] version [0.5.6-2]

DC/OS Zeppelin is being installed!



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

```
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

  1. [Delete the ACS DC/OS cluster](cleanup/README.md)
