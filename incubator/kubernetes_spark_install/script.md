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

# Connect to the Kubernetes cluster

```
az acs kubernetes get-credentials --resource-group=$ACS_RESOURCE_GROUP --name=$ACS_CLUSTER_NAME
kubectl proxy --port=8001 &
```

# Deploy Spark and Zeppelin

We can create a new Spark cluster through the Kubernetes cli and the
`yml` files found in the `~/.acs/demo/kubernetes/examples/spark` directory
(these were checked out for you by the preparation script). 

```
kubectl create -f ~/.acs/demo/kubernetes/examples/spark
```

Results:

```
namespace "spark-cluster" created
replicationcontroller "spark-master-controller" created
service "spark-master" created
replicationcontroller "spark-ui-proxy-controller" created
service "spark-ui-proxy" created
replicationcontroller "spark-worker-controller" created
replicationcontroller "zeppelin-controller" created
service "zeppelin" created
```

Once that is set up, ensure everything is running normally.

```
kubectl get pods
```

Results:

```
NAME                              READY     STATUS              RESTARTS   AGE
spark-master-controller-169rl     1/1       Starting            0          1s
spark-ui-proxy-controller-9zclb   1/1       Starting            0          1s
spark-worker-controller-8whcj     1/1       Starting            0          1s
spark-worker-controller-wr1g5     1/1       Starting            0          1s
zeppelin-controller-nd71c         1/1       Starting            0          1s
```

Next, we will check the load balancer endpoints.

```
kubectl get svc -o wide
```

Results:

```
NAME             CLUSTER-IP     EXTERNAL-IP    PORT(S)             AGE       SELECTOR
kubernetes       10.0.0.1       <none>         443/TCP             1m        <none>
spark-master     10.0.226.162   <none>         7077/TCP,8080/TCP   1m        component=spark-master
spark-ui-proxy   10.0.25.240    52.168.86.47   80:32466/TCP        1m        component=spark-ui-proxy
zeppelin         10.0.82.171    <pending>      80:30624/TCP        1m        component=zeppelin
```

## Enable the Kubernetes and Spark UI

To view the Kubernetes and Spark UI from a browser, we can use the cli
to configure a proxy.

```
kubectl proxy --port=8001 &
```

Results:

```
Starting to serve on 127.0.0.1:8001
```

Kubernetes will be available
at [http://localhost:8001/ui](http://localhost:8001/ui).

Spark will be available
at
[http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/](http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/).

```
# Open the Kubernetes UI at http://localhost:8001/ui
# Open the Spark UI at http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/
```

## Enable Zeppeling UI

For Zeppelin, we can use the cli to configure a port forward and run
it as a background process. We will need the unique string from the
Zeppelin pod. This is visibile in the UI dashboard or can be retrieved
from the CLI:

```
kubectl get pods
```

Results:

```
NAME                              READY     STATUS              RESTARTS   AGE
spark-master-controller-169rl     1/1       Running             0          3m
spark-ui-proxy-controller-9zclb   1/1       Running             0          3m
spark-worker-controller-8whcj     1/1       Running             0          3m
spark-worker-controller-wr1g5     1/1       Running             0          3m
zeppelin-controller-nd71c         1/1       Running             0          3m
```

Once we have that we can setup the port forwarder:

```
kubectl port-forward zeppelin-controller-nd71c 8080:8080 &
```

Results:

```
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080
```

*Note: Replace `nd71c` with the deployment specific id from the result
of `zeppelin-controller` from `kubectl get pods`*

The Zeppelin interface will be available
at [http://localhost8080](http://localhost8080)

```
xdg-open http://localhost:8080
```
