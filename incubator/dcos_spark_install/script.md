# What is Spark?

[Spark](https://spark.apache.org/) is a powerful general cluster computing system for Big Data. We will be using [DC/OS](https://dcos.io/) to deploy a Spark cluster. We will also install [Zeppelin](https://zeppelin.apache.org/), a web-based notebook for data analytics, making it easier to interact with Spark.

It is assumed that you have prepared the demo environment by running
`prep.sh`, if not you need to break from this script and run it now.

At this point, the DC/OS interface should be available at [https://localhost](https://localhost).

# Deploying Spark

We can use the DC/OS cli to set up Spark.

```
dcos package install spark --yes
```

*Note: you need to have virtualenv set up to install the Spark package (`sudo pip install virtualenv`).*

Once Spark is deployed, it will be available at [http://localhost/service/spark/](http://localhost/service/spark/).

Next, we can deploy Zeppelin.

Once deployed, Zeppelin will be available at [http://localhost/service/spark/](http://localhost/service/spark/)

````
dcos package install zeppelin --yes
```
