# What is Apache Spark?

[Apache Spark](https://spark.apache.org/) is a powerful general cluster computing system for Big Data. We will be using [DC/OS](https://dcos.io/) to deploy a Spark cluster. We will also install [Zeppelin](https://zeppelin.apache.org/), a web-based notebook for data analytics, making it easier to interact with Spark.

# Deploy Spark

It is assumed that you have prepared the demo environment by running `prep.sh`, if not you need to break from this script and run it now.

We can create a new Spark cluster through the Kubernetes cli and the `yml` files found in the `~/kubernetes/examples/spark` directory. We will also spin up Zeppelin.

```
kubectl create -f ~/kubernetes/examples/spark
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
spark-master-controller-169rl     1/1       Running             0          1h
spark-ui-proxy-controller-9zclb   1/1       Running             0          1h
spark-worker-controller-8whcj     1/1       Running             0          1h
spark-worker-controller-wr1g5     1/1       Running             0          1h
zeppelin-controller-nd71c         1/1       Running             0          1h
```

Next, we will check the load balancer endpoints.

```
kubectl get svc -o wide
```

Results:

```
NAME             CLUSTER-IP     EXTERNAL-IP    PORT(S)             AGE       SELECTOR
kubernetes       10.0.0.1       <none>         443/TCP             1h        <none>
spark-master     10.0.226.162   <none>         7077/TCP,8080/TCP   1h        component=spark-master
spark-ui-proxy   10.0.25.240    52.168.86.47   80:32466/TCP        1h        component=spark-ui-proxy
zeppelin         10.0.82.171    <pending>      80:30624/TCP        1h        component=zeppelin
```

## Enable the Kubernetes, Spark and Zeplin UI

To view the Kubernetes and Spark UI from a browser, we can use the cli to configure a proxy.

```
kubectl proxy --port=8001
```

Results:

```
Starting to serve on 127.0.0.1:8001
```

Kubernetes will be available at [http://localhost:8001/ui](http://localhost:8001/ui).  
Spark will be available at [http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/](http://localhost:8001/api/v1/proxy/namespaces/spark-cluster/services/spark-ui-proxy/).

For Zeppelin, we can use the cli to configure a port forward and run it as a background process.

```
kubectl port-forward zeppelin-controller-nd71c 8080:8080 &
```

*Note: Replace `nd71c` with the deployment specific id from the result of `zeppelin-controller` from `kubectl get pods`*

Results:

```
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080
```

The Zeppelin interface will be available at [http://localhost8080](http://localhost8080)
