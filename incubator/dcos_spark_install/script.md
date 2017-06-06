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

## What is Azure Container Service?

Azure Container Service optimizes the configuration of popular
open-source tools and technologies specifically for Azure. You get an
open solution that offers portability for both your containers and
your application configuration. You select the size, number of hosts,
and choice of orchestrator tools—Container Service handles everything
else.

# Preparation

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it
now. This will have pre-created a cluster with the following
configuration:

```
env | grep ACS_.*
```

We first need to ensure that we can connect to the DC/OS masters by
opening an SSH tunnel:

```
sudo ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com -i ~/.ssh/id_rsa &
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

# Services

DC/OS provides an easy way to manage services that will help schedule
and manage workloads for you. By default DC/OS provides two
services. Marathon is for scheduling long running jobs while Metronome
is for managing scheduled jobs.

We can view the services running using the DC/OS CLI:

```
dcos service
```

Results:

```
NAME          HOST     ACTIVE  TASKS  CPU  MEM  DISK  ID
marathon   172.16.0.5   True     0    0.0  0.0  0.0   5ae8b6c8-c88b-4b8a-8d45-9b4b063be9e6-0001
metronome  172.16.0.5   True     0    0.0  0.0  0.0   5ae8b6c8-c88b-4b8a-8d45-9b4b063be9e6-0000
```

## Deploying the Apache Spark service

The DC/OS CLI makes it really easy to install Spark.

```
dcos package install spark --yes --cli
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
Continue installing? [yes/no] yes
Installing Marathon app for package [zeppelin] version [0.5.6]

        Documentation: https://github.com/dcos/examples/tree/master/1.8/zeppelin/
	    Issues: https://dcos.io/community/				
```

Once deployed, Zeppelin will be available
at [http://localhost:10000/service/spark/](http://localhost:10000/service/spark/)

# Working with Spark

As part of the Spark installation DC/OS has installed the Spark CLI.

```
dcos spark 
```

Results: Expected similarity: 0.6

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

