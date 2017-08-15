# Validate an ACS Kubernetes Cluster

Check the cluster has been created:

```
az acs wait --resource-group $ACS_RESOURCE_GROUP --name $ACS_CLUSTER_NAME --created --timeout 15
```

If the cluster has been successfully created this command returns nothing.

Results:

```
```

# Get the manifest file for our test application

```
curl -o $SIMDEM_TEMP_DIR/azure-vote-all-in-one.yaml https://raw.githubusercontent.com/Azure-Samples/azure-voting-app/master/kubernetes-manifests/azure-vote-all-in-one.yaml
```
Results:

```
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  3265  100  3265    0     0  7827      0 --:--:-- --:--:-- --:--:--   7811
```

# Ensure that our test service is not currently deployed

```
kubectl delete -f ~/.simdem/tmp/azure-vote-all-in-one.yaml 
```

# Deploy the service

```
kubectl create -f $SIMDEM_TEMP_DIR/azure-vote-all-in-one.yaml
```

Results:

```
storageclass "slow" created
persistentvolumeclaim "mysql-pv-claim" created
secret "azure-vote" created
deployment "azure-vote-back" created
service "azure-vote-back" created
deployment "azure-vote-front" created
service "azure-vote-front" created
```

# Wait for IP to become available

```
IP=""
while [ -z $IP ]; do sleep 10; IP=$(kubectl get service vamp -o jsonpath="{.status.loadBalancer.ingress[*].ip}"); done
```

# Ensure the Container is Responding

```
curl -I $IP
```

Results:

```

```

# Cleanup

```
kubectl delete -f ~/.simdem/tmp/azure-vote-all-in-one.yaml 
rm $SIMDEM_TEMP_DIR/azure-vote-all-in-one.yaml
```

