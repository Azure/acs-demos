
Requirements
A working az command-line client
A Kubernetes cluster with a working kubectl
Quickstart
Edit examples/aci-connector.yaml and supply environment variables
Run the ACI Connector with kubectl create -f examples/aci-connector.yaml
Wait for kubectl get nodes to display the aci-connector node
Run an NGINX pod via ACI using kubectl create -f examples/nginx-pod.yaml
Access the NGINX pod via its public address
Usage
Create a Resource Group
The ACI Connector will create each container instance in a specified resource group. You can create a new resource group with:
az group create -n aci-test -l westus
{
  "id": "/subscriptions/<subscriptionId>/resourceGroups/aci-test",
  "location": "westus",
  "managedBy": null,
  "name": "aci-test",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null
}
Edit the examples/aci-connector.yaml and put the name of the resource group into the ACI_RESOURCE_GROUP environment variable.
Create a Service Principal
A service principal is required to allow the ACI Connector to create resources in your Azure subscription. You can create one using the az CLI using the instructions below.
Find your subscriptionId with the az CLI:
```
az account list -o table
```
Name                                             CloudName    SubscriptionId                        State    IsDefault
-----------------------------------------------  -----------  ------------------------------------  -------  -----------
Pay-As-You-Go                                    AzureCloud   12345678-9012-3456-7890-123456789012  Enabled  True
Use az to create a Service Principal that can perform operations on your resource group:
```
az ad sp create-for-rbac --role=Contributor --scopes /subscriptions/<subscriptionId>/resourceGroups/aci-test
```
{
  "appId": "<redacted>",
  "displayName": "azure-cli-2017-07-19-19-13-19",
  "name": "http://azure-cli-2017-07-19-19-13-19",
  "password": "<redacted>",
  "tenant": "<redacted>"
}
Edit the examples/aci-connector.yaml and input environment variables using the values above:
AZURE_CLIENT_ID: insert appId
AZURE_CLIENT_KEY: insert password
AZURE_TENANT_ID: insert tenant
AZURE_SUBSCRIPTION_ID: insert subscriptionId
Install the ACI Connector
```
kubectl create -f examples/aci-connector.yaml 
```
deployment "aci-connector" created
```
kubectl get nodes -w
```
NAME                        STATUS                     AGE       VERSION
aci-connector               Ready                      3s        1.6.6
k8s-agentpool1-31868821-0   Ready                      5d        v1.7.0
k8s-agentpool1-31868821-1   Ready                      5d        v1.7.0
k8s-agentpool1-31868821-2   Ready                      5d        v1.7.0
k8s-master-31868821-0       Ready,SchedulingDisabled   5d        v1.7.0
Install the NGINX example
```
kubectl create -f examples/nginx-pod.yaml 
```
pod "nginx" created
```
kubectl get po -w -o wide
```
NAME          READY     STATUS    RESTARTS   AGE       IP             NODE
aci-connector-3396840456-v75q2  1/1       Running   0          44s       10.244.2.21    k8s-agentpool1-31868821-2
nginx         1/1       Running   0          31s       13.88.27.150   aci-connector
Note the pod is scheduled on the aci-connector node. It should now be accessible at the public IP listed.
Using the Kubernetes scheduler
The example in nginx-pod hard codes the node name, but you can also use the Kubernetes scheduler.
The virtual aci node, has a taint (azure.com/aci) with a default effect of NoSchedule. This means that by default Pods will not schedule onto the aci node unless they are explicitly placed there.
However, if you create a Pod that tolerates this taint, it can be scheduled to the aci node by the Kubernetes scheduler.
Here is an example of Pod with this toleration.
To use this Pod, you can simply:
```
kubectl create -f examples/nginx-pod-toleration.yaml
```
Note that if you have other nodes in your cluster then this Pod may not necessarily schedule onto the Azure Container Instances.
To force a Pod onto Azure Container Instances, you can either explicitly specify the NodeName as in the first example, or you can delete all of the other nodes in your cluster using kubectl delete nodes <node-name>. A third option is to fill your cluster with other workloads, then the scheduler will be obligated to schedule work to the Azure Container Instance API.
*Taken from https://github.com/Azure/aci-connector-k8s
