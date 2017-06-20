# What are we going to do?

We'll install Apache Spark and Apache Zeppelin on Azure Container
Service with Kubernetes.

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it now.

# What is Apache Spark?

[Apache Spark](https://spark.apache.org/) is a powerful general
cluster computing system for Big Data. We will be
using [Kubernetes](https://kubernetes.io/) to deploy a Spark cluster.

## What is Apache Zeppelin?
 
[Apache Zepellin](http://zeppelin.apache.org/) is a web-based notebook
that enables interactive data analytics. It has excellent integration
with Apache Spark.

## What is Azure Container Service?

Azure Container Service optimizes the configuration of popular
open-source tools and technologies specifically for Azure. You get an
open solution that offers portability for both your containers and
your application configuration. You select the size, number of hosts,
and choice of orchestrator tools—Container Service handles everything
else.

# Next Steps

  1. [Create a Kubernetes cluster](preparation/script.md)
  2. [Install Apache Spark](install/script)
  3. [Delete the cluster](cleanup/script.md)
