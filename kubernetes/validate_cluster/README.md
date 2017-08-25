# Validate an ACS Kubernetes Cluster

Check the cluster has been created:

```
az acs wait --resource-group $SIMDEM_RESOURCE_GROUP --name $SIMDEM_CLUSTER_NAME --created --timeout 15
```

If the cluster has been successfully created this command returns nothing.

Results:

```
```

# Ensure that our test service is not currently deployed

```
kubectl delete -f ./hello-world.yaml
```

# Deploy the service

```
kubectl create -f ./hello-world.yaml
```

Results:

```
deployment "hello-world" created
service "hello-world" created
```

# Wait for IP to become available

```
IP=""
while [ -z "$IP" ]; do sleep 2; IP=$(kubectl get service hello-world -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```

```
echo "Hello World application should be available at $IP"
```

# Ensure the Container is Responding

```
curl -I $IP --max-time 10
```

Results:

```
HTTP/1.1 200 OK
Server: nginx/1.8.0
Date: Wed, 16 Aug 2017 06:13:32 GMT
Content-Type: text/html; charset=UTF-8
Connection: keep-alive
X-Powered-By: PHP/5.6.14
```

# Cleanup

```
kubectl delete -f ./hello-world.yaml
```

