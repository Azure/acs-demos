# What are we going to do?

We'll install Apache Spark and Apache Zeppelin on Azure Container
Service with Kubernetes.

# Connect to the Kubernetes cluster

```
az acs kubernetes get-credentials --resource-group=$SIMDEM_RESOURCE_GROUP --name=$SIMDEM_CLUSTER_NAME
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
spark-master-controller-ktzct     0/1       ContainerCreating   0          2s
spark-ui-proxy-controller-z3v7h   0/1       ContainerCreating   0          2s
spark-worker-controller-n5m6b     0/1       ContainerCreating   0          2s
spark-worker-controller-ndhkj     0/1       ContainerCreating   0          2s
zeppelin-controller-bff22         0/1       ContainerCreating   0          2s
```

You can use `kubectl get pods --watch` to watch for changes and once you
see all 5 containers are running you can move forwards. Alternatively,
if you are scripting the deployment you will want to block until all 5
pods are running are running. You can do this with.

```
NAME                              READY     STATUS    RESTARTS   AGE
spark-master-controller-ktzct     1/1       Running   0          1m
spark-ui-proxy-controller-z3v7h   1/1       Running   0          1m
spark-worker-controller-n5m6b     1/1       Running   0          1m
spark-worker-controller-ndhkj     1/1       Running   0          1m
zeppelin-controller-bff22         1/1       Running   0          1m
```

Next, we will check the load balancer endpoints for the two UI
services (spark-ui-proxy and zeppelin).

```
kubectl get services
```

Results:

```
NAME             CLUSTER-IP     EXTERNAL-IP    PORT(S)             AGE
kubernetes       10.0.0.1       <none>         443/TCP             1m
spark-master     10.0.226.162   <none>         7077/TCP,8080/TCP   1m
spark-ui-proxy   10.0.25.240    <pending>      80:32466/TCP        1m
zeppelin         10.0.82.171    <pending>      80:30624/TCP        1m
```

As before, you can use the `--watch` flag on this command to watch for
changes, once both External-IPs are available you can
proceed. Alternatively, if you are scripting it can be useful to block
until they are available. One way to do this is as follows, this has
the added advantage of putting the IP into an environment variable for
later use.

```
SPARK_IP=""
while [ -z $SPARK_IP ]; do sleep 10; SPARK_IP=$(kubectl get service spark-ui-proxy -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
echo "Apache Spark UI is at http://$SPARK_IP"
```

and for Apache Zeppelin:

```
ZEPPELIN_IP=""
while [ -z $ZEPPELIN_IP ]; do sleep 10; ZEPPELIN_IP=$(kubectl get service zeppelin -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
echo "Apache Zeppelin UI is at http://$ZEPPELIN_IP"
```

Now we can access the Spark UI with a web browser:


```
curl -I http://$SPARK_IP
```

Results:

```
HTTP/1.1 200 OK
Content-Type: text/html
Last-Modified: Mon, 11 Jan 2016 23:08:48 GMT
Content-Length: 2697
Accept-Ranges: bytes
Server: Jetty(8.1.14.v20131031)
```

And the Zeppelin UI is available at:

```
curl -I http://$ZEPPELIN_IP
```

Results:

```
HTTP/1.1 200 OK
Content-Type: text/html
Last-Modified: Mon, 11 Jan 2016 23:08:48 GMT
Content-Length: 2697
Accept-Ranges: bytes
Server: Jetty(8.1.14.v20131031)

```


