# What is Spark?

Spark is a powerful general cluster computing system for Big Data. We will be using DC/OS to deploy a Spark cluster.

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it now.

At this point, the DC/OS interface should be available at [https://localhost](https://localhost).

# Deploying Spark

We can use the DC/OS cli to set up Spark.

```
dcos package install spark --yes
```

*Note: you need to have virtualenv set up to install the Spark package (`sudo pip install virtualenv`).*

Next, we can deploy Zeppelin.

```
dcos package install zeppelin --yes
```
