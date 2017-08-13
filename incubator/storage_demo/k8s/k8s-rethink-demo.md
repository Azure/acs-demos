# RethinkDB Storage Demo on Kubernetes

This demo shows the use of persistent volumes in Kubernetes for storing RethinkRB state.  In this demo, the following will be covered:

- Deployment of a Kubernetes cluster on Azure
- The structure of the YAML file for deploying RethinkDB
- The deployment of the RethinkDB service
- Demonstrate the node RethinkDB is running on
- Interact with RethinkDB:
    - Create a table
    - Insert some data
    - Show the table
- Destroy the RethinkDB pod
- Show redeployment of RethinkDB service
- Show data previously inserted is still present

## Deploying a Kubernetes cluster

To deploy the Kubernetes cluster, it is assumed that you have the [Azure CLI](https://github.com/Azure/azure-cli) installed and logged in.

To deploy the cluster, we need to:

- Create a resource group
- Deploy the cluster

Create the resource group named `jmsbck8rg` in `westus` as follows:

```bash
jims@azhat:~$ az group create --name "jmsbck8rg" --location="westus"
{
  "id": "/subscriptions/04f7ec88-8e28-41ed-8537-5e17766001f5/resourceGroups/jmsbck8rg",
  "location": "westus",
  "managedBy": null,
  "name": "jmsbck8rg",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null
}
```

For the cluster, we will use the following settings:

- `admin-username`: `sadmin`
- `agent-count`:    `3`
- `agent-vm-size`:  `Standard_D3_v2`
- `master-count`:   `1`
- `dns-prefix`:     `jsk8`
- `name`:           `acsbck8s`

```bash
jims@azhat:~$ az acs create --name="acsbck8s" --resource-group="jmsbck8rg" --admin-username=sadmin --agent-count=3 --agent-vm-size="Standard_D3_v2" --dns-prefix="jsk8" --location="westus" --master-count=1 --orchestrator-type=kubernetes --ssh-key-value @/home/jims/.ssh/id_jmsk8s_rsa.pub
waiting for AAD role to propagate.done
{
  "id": "/subscriptions/04f7ec88-8e28-41ed-8537-5e17766001f5/resourceGroups/jmsbck8rg/providers/Microsoft.Resources/deployments/azurecli1486924695.6415310",
  "name": "azurecli1486924695.6415310",
  "properties": {
    "correlationId": "6a23ec86-3b7d-45e0-84d1-6db8c5730237",
    "debugSetting": null,
    "dependencies": [],
    "mode": "Incremental",
    "outputs": null,
    "parameters": {
      "clientSecret": {
        "type": "SecureString"
      }
    },
    "parametersLink": null,
    "providers": [
      {
        "id": null,
        "namespace": "Microsoft.ContainerService",
        "registrationState": null,
        "resourceTypes": [
          {
            "aliases": null,
            "apiVersions": null,
            "locations": [
              "westus"
            ],
            "properties": null,
            "resourceType": "containerServices"
          }
        ]
      }
    ],
    "provisioningState": "Succeeded",
    "template": null,
    "templateLink": null,
    "timestamp": "2017-02-12T18:43:24.096870+00:00"
  },
  "resourceGroup": "jmsbck8rg"
}
```

In order to interact with the Kubernetes cluster, we need to retrieve the `kube.config` file:

```bash
jims@azhat:~$ az acs kubernetes get-credentials --name=acsbck8s --resource-group=jmsbck8rg --ssh-key-file=/home/jims/.ssh/id_jmsk8s_rsa
```

It is also assumed that you have `kubectl` installed at this point.

To verify the cluster is running and is accessible:

```bash
jims@azhat:~$ kubectl get all
NAME             CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
svc/kubernetes   10.0.0.1     <none>        443/TCP   4m
```

## Configuring and deploying RethinkDB

[RethinkDB](https://github.com/rethinkdb/rethinkdb) is an open-source NoSQL JSON document database.  For this next part, we will setup the YAML configuration file such that RethinkDB is deployed with an associated persistent volume.  This is done by generating the YAML configuration file.

To define a persistent volume on Kubernetes, there are three parts one needs to configure:

- Storage Class
- Persistent Volume Claim
- Associate the Volume Claim with the service and assign a mountpoint

The Storage Class would resemble as follows:

```bash
kind: StorageClass
apiVersion: storage.k8s.io/v1beta1
metadata:
  name: slow
  namespace: rethinkdb
provisioner: kubernetes.io/azure-disk
parameters:
  skuName: Standard_LRS
```

In this case, a storage class named `slow` is specified to use the `azure-disk` provisioner.

A Persistent Volume Claim defines the type (Storage Class), size, and name of a vollume to generate.  The configuration resembles:

```bash
apiVersion: "v1"
kind: "PersistentVolumeClaim"
metadata:
  name: "rethinkdb-pv"
  namespace: "rethinkdb"
  annotations:
    volume.beta.kubernetes.io/storage-class: "slow"
spec:
  accessModes:
    - "ReadWriteOnce"
  resources:
    requests:
      storage: "10Gi"
```

Finally, once a volume claim has been defined, it has to be associated with the deployment template:

```bash
      containers:
      - name: master
        image: rethinkdb:2.3.5
        command: [ "rethinkdb" ]
        args: [ "--bind", "all", "-d", "/data/rethinkdb_home/db" ]
        ports:
        - containerPort: 8080
        - containerPort: 28015
        volumeMounts:
        - mountPath: /data/rethinkdb_home
          name: rethinkdb-home
        resources:
          limits:
            cpu: 1000m
            memory: 8000Mi
          requests:
            cpu: 1000m
            memory: 8000Mi
      volumes:
      - name: rethinkdb-home
        persistentVolumeClaim:
          claimName: rethinkdb-pv
```

The Persistent Volume Claim is associated with a volume and that volume is then given a mountpoint in the container.

Note, for this demo, we are deploying RethinkDB version 2.3.5 which has an existing Docker image.  The complete YAML file for deploying RethinkDB is as follows:

```bash
---
kind: Namespace
apiVersion: v1
metadata:
  name: rethinkdb
---
kind: StorageClass
apiVersion: storage.k8s.io/v1beta1
metadata:
  name: slow
  namespace: rethinkdb
provisioner: kubernetes.io/azure-disk
parameters:
  skuName: Standard_LRS
---
apiVersion: "v1"
kind: "PersistentVolumeClaim"
metadata:
  name: "rethinkdb-pv"
  namespace: "rethinkdb"
  annotations:
    volume.beta.kubernetes.io/storage-class: "slow"
spec:
  accessModes:
    - "ReadWriteOnce"
  resources:
    requests:
      storage: "10Gi"
---
# [START rethinkdb_deployment]
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: rethinkdb
  namespace: rethinkdb
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: master
    spec:
      containers:
      - name: proxy
        image: gcr.io/google_containers/kubectl-amd64:v1.5.2
        command:
        - proxy
      containers:
      - name: master
        image: rethinkdb:2.3.5
        command: [ "rethinkdb" ]
        args: [ "--bind", "all", "-d", "/data/rethinkdb_home/db" ]
        ports:
        - containerPort: 8080
        - containerPort: 28015
        volumeMounts:
        - mountPath: /data/rethinkdb_home
          name: rethinkdb-home
        resources:
          limits:
            cpu: 1000m
            memory: 8000Mi
          requests:
            cpu: 1000m
            memory: 8000Mi
      volumes:
      - name: rethinkdb-home
        persistentVolumeClaim:
          claimName: rethinkdb-pv
# [END rethinkdb_deployment]
# [START rethinkdb_service]
---
  kind: Service
  apiVersion: v1
  metadata:
    name: rethinkdb
    namespace: rethinkdb
  spec:
    type: LoadBalancer
    selector:
      app: master
    ports:
      - protocol: TCP
        port: 8080
        targetPort: 8080
        name: rethinkdb-ui-port
      - protocol: TCP
        port: 28015
        targetPort: 28015
        name: rethinkdb-admin-port        
# [END rethinkdb_service]
```

The deployment exposes two external ports `8080` (the RethinkDB UI) and `28015` which is the port RethinkDB clients connect to.  With this deployment, the ports are publicly accessible and probably should be more controlled / contained in a production environment.

Using `kubectl`, the YAML file `rethinkdb_pv.yaml` can be deployed as follows:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ kubectl create -f ./rethink/rethinkdb_pv.yaml 
namespace "rethinkdb" created
storageclass "slow" created
persistentvolumeclaim "rethinkdb-pv" created
deployment "rethinkdb" created
service "rethinkdb" created
```

Azure disk operations, especially in the current version of Kubernetes deployed to ACS, can take some time.  The provisioning and formatting of the Persistent Volume may take upwards of ten minutes.

To check the status of the RethinkDB deployment:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ kubectl --namespace=rethinkdb get pods
NAME                        READY     STATUS              RESTARTS   AGE
rethinkdb-111383062-m3b76   0/1       ContainerCreating   0          1m
```

Waiting awhile, you should eventually see:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ kubectl --namespace=rethinkdb get pods
NAME                        READY     STATUS    RESTARTS   AGE
rethinkdb-111383062-m3b76   1/1       Running   0          19m
```

If one wanted to see more detail about the pod `rethinkdb-111383062-m3b76`, you could use the `describe` command with `kubectl` as follows:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ kubectl --namespace=rethinkdb describe pod rethinkdb-111383062-m3b76 
Name:		rethinkdb-111383062-m3b76
Namespace:	rethinkdb
Node:		k8s-agent-ea300e7b-2/10.240.0.5
Start Time:	Mon, 13 Feb 2017 08:54:38 -0800
Labels:		app=master
		pod-template-hash=111383062
Status:		Running
IP:		10.244.3.3
Controllers:	ReplicaSet/rethinkdb-111383062
Containers:
  master:
    Container ID:	docker://7f3a3b34da008a3a3dc4bb6028fd360b2b1f8449e3b4c02e2a1df1edb30fbb1d
    Image:		rethinkdb:2.3.5
    Image ID:		docker-pullable://rethinkdb@sha256:4308852f1d72beeeb7b47b6c715e2df0f1d8f7063a2dc5b6c6de430e29e1bb58
    Ports:		8080/TCP, 28015/TCP
    Limits:
      cpu:	1
      memory:	8000Mi
    Requests:
      cpu:		1
      memory:		8000Mi
    State:		Running
      Started:		Mon, 13 Feb 2017 09:04:40 -0800
    Ready:		True
    Restart Count:	0
    Volume Mounts:
      /var/rethinkdb_home from rethinkdb-home (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-c1wbq (ro)
    Environment Variables:	<none>
Conditions:
  Type		Status
  Initialized 	True 
  Ready 	True 
  PodScheduled 	True 
Volumes:
  rethinkdb-home:
    Type:	PersistentVolumeClaim (a reference to a PersistentVolumeClaim in the same namespace)
    ClaimName:	rethinkdb-pv
    ReadOnly:	false
  default-token-c1wbq:
    Type:	Secret (a volume populated by a Secret)
    SecretName:	default-token-c1wbq
QoS Class:	Guaranteed
Tolerations:	<none>
Events:
  FirstSeen	LastSeen	Count	From				SubObjectPath		Type		Reason			Message
  ---------	--------	-----	----				-------------		--------	------			-------
  21m		21m		2	{default-scheduler }					Warning		FailedScheduling	[SchedulerPredicates failed due to PersistentVolumeClaim is not bound: "rethinkdb-pv", which is unexpected., SchedulerPredicates failed due to PersistentVolumeClaim is not bound: "rethinkdb-pv", which is unexpected., SchedulerPredicates failed due to PersistentVolumeClaim is not bound: "rethinkdb-pv", which is unexpected.]
  21m		21m		1	{default-scheduler }					Normal		Scheduled		Successfully assigned rethinkdb-111383062-m3b76 to k8s-agent-ea300e7b-2
  19m		12m		4	{kubelet k8s-agent-ea300e7b-2}				Warning		FailedMount		Unable to mount volumes for pod "rethinkdb-111383062-m3b76_rethinkdb(1a48f241-f20d-11e6-a031-000d3a317561)": timeout expired waiting for volumes to attach/mount for pod "rethinkdb-111383062-m3b76"/"rethinkdb". list of unattached/unmounted volumes=[rethinkdb-home]
  19m		12m		4	{kubelet k8s-agent-ea300e7b-2}				Warning		FailedSync		Error syncing pod, skipping: timeout expired waiting for volumes to attach/mount for pod "rethinkdb-111383062-m3b76"/"rethinkdb". list of unattached/unmounted volumes=[rethinkdb-home]
  11m		11m		1	{kubelet k8s-agent-ea300e7b-2}	spec.containers{master}	Normal		Pulling			pulling image "rethinkdb:2.3.5"
  11m		11m		1	{kubelet k8s-agent-ea300e7b-2}	spec.containers{master}	Normal		Pulled			Successfully pulled image "rethinkdb:2.3.5"
  11m		11m		1	{kubelet k8s-agent-ea300e7b-2}	spec.containers{master}	Normal		Created			Created container with docker id 7f3a3b34da00; Security:[seccomp=unconfined]
  11m		11m		1	{kubelet k8s-agent-ea300e7b-2}	spec.containers{master}	Normal		Started			Started container with docker id 7f3a3b34da00
```

## Interacting with RethinkDB

In order to connect to RethinkDB, it is necessary to determine the IP address and ports RethinkDB is accessible from.  From the YAML file above, the service defined ports 8080 and 28015, to determine the IP address for the service and verify that the ports are indeed open:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ kubectl --namespace=rethinkdb get svc
NAME        CLUSTER-IP    EXTERNAL-IP   PORT(S)                          AGE
rethinkdb   10.0.52.247   13.64.114.4   8080:30180/TCP,28015:31336/TCP   28m
```

The above indicated that the external IP address of the service is `13.64.114.4` and that, indeed, ports 8080 and 28105 are open.  To verify, we could run `wget` as follows:

```bash
jims@azhat:~/src/github/acsbc_storage/k8s$ wget http://13.64.114.4:8080
--2017-02-13 09:24:45--  http://13.64.114.4:8080/
Connecting to 13.64.114.4:8080... connected.
HTTP request sent, awaiting response... 200 OK
Length: unspecified [text/html]
Saving to: ‘index.html’

index.html                             [ <=>                                                             ]   3.21K  --.-KB/s    in 0.001s  

2017-02-13 09:24:45 (2.88 MB/s) - ‘index.html’ saved [3291]
```

So, now it is time to interact with RethinkDB.  To do this, a simple NodeJS application is developed using the RethinkDB client library.  It's usage is as follows:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ node ./index.js --help
Usage:
  index.js [OPTIONS] [ARGS]

Options: 
  -h, --host [STRING]    RethinkDB hostname (Default is localhost)
  -p, --port [NUMBER]    RethinkDB port (Default is 8080)
  -a, --action [STRING]  Action to perform (create, insert, get, delete)  (Default is get)
  -t, --table STRING     Table name
  -f, --datafile STRING  RethinkDB datafile
```

In order to use the app, it is assumed that you have NodeJS installed.  Also, in order to actually use it, the required modules need to be installed as well.  To do that:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ npm install .
rethink_test.js@0.0.1 /home/jims/src/github/acsbc_storage/app
├─┬ cli@1.0.1 
│ ├── exit@0.1.2 
│ └─┬ glob@7.1.1 
│   ├── fs.realpath@1.0.0 
│   ├─┬ inflight@1.0.6 
│   │ └── wrappy@1.0.2 
│   ├── inherits@2.0.3 
│   ├─┬ minimatch@3.0.3 
│   │ └─┬ brace-expansion@1.1.6 
│   │   ├── balanced-match@0.4.2 
│   │   └── concat-map@0.0.1 
│   ├── once@1.4.0 
│   └── path-is-absolute@1.0.1 
└─┬ rethinkdb@2.3.3 
  └── bluebird@2.11.0 

npm WARN rethink_test.js@0.0.1 No repository field.
```

So, using the app, let's interact with RethinkDB.  First, create a table named `weather`:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ node ./index.js -h 13.64.114.4 -p 28015 -a create -t weather
{
  "config_changes": [
    {
      "new_val": {
        "db": "test",
        "durability": "hard",
        "id": "6adf018b-c359-4a42-87b7-3dac25757aa1",
        "indexes": [],
        "name": "weather",
        "primary_key": "id",
        "shards": [
          {
            "nonvoting_replicas": [],
            "primary_replica": "rethinkdb_111383062_m3b76_vjx",
            "replicas": [
              "rethinkdb_111383062_m3b76_vjx"
            ]
          }
        ],
        "write_acks": "majority"
      },
      "old_val": null
    }
  ],
  "tables_created": 1
}
```

Adding data to the table `weather`:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ node ./index.js -h 13.64.114.4 -p 28015 -a insert -t weather -f ./data/sfo_status.json 
{
  "deleted": 0,
  "errors": 0,
  "generated_keys": [
    "318306fd-efd6-47dd-b52f-c989bf0c15fe"
  ],
  "inserted": 1,
  "replaced": 0,
  "skipped": 0,
  "unchanged": 0
}
```

And finally showing the data:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ node ./index.js -h 13.64.114.4 -p 28015 -a get -t weather
[
  {
    "IATA": "SFO",
    "ICAO": "KSFO",
    "city": "San Francisco",
    "delay": "true",
    "id": "318306fd-efd6-47dd-b52f-c989bf0c15fe",
    "name": "San Francisco International",
    "state": "California",
    "status": {
      "avgDelay": "1 hour and 48 minutes",
      "closureBegin": "",
      "closureEnd": "",
      "endTime": "",
      "maxDelay": "",
      "minDelay": "",
      "reason": "WEATHER / LOW CEILINGS",
      "trend": "",
      "type": "Ground Delay"
    },
    "weather": {
      "meta": {
        "credit": "NOAA's National Weather Service",
        "updated": "5:56 PM Local",
        "url": "http://weather.gov/"
      },
      "temp": "63.0 F (17.2 C)",
      "visibility": 10,
      "weather": "Mostly Cloudy",
      "wind": "South at 13.8mph"
    }
  }
]
```

It should be noted that the output of the `create` and `insert` commands were the results returned by RethinkDB after each operation.  The content shown for `get` is the content stored in `./data/sfo_status.json` that was inserted into the database.

## Clobbering the RethinkDB pod and waiting for a new one

Now that we have data in the RethinkDB, let's go about clobbering the RethinkDB pod and see that the data persists.  In order to do this, we first need to determine the name of the RethinkDB pod and the agent that it is running on.  This will more easily show that indeed the pod has moved and the data is still there.

To determin the pod name:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb get pods
NAME                        READY     STATUS    RESTARTS   AGE
rethinkdb-111383062-m3b76   1/1       Running   0          1h
```

The pod name is `rethinkdb-111383062-m3b76`.  To determine the node that it is running on:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb describe pod rethinkdb-111383062-m3b76 | grep Node
Node:		k8s-agent-ea300e7b-2/10.240.0.5
```

So, the pod is currently running on agent `k8s-agent-ea300e7b-2`.

Clobbering the pod is as simple as:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb delete pod rethinkdb-111383062-m3b76
pod "rethinkdb-111383062-m3b76" deleted
```

Kubernetes will try and start a new RethinkDB pod:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb get pods
NAME                        READY     STATUS              RESTARTS   AGE
rethinkdb-111383062-9rp0f   0/1       ContainerCreating   0          29s
```

The new pod is named `rethinkdb-111383062-9rp0f`.  

The new pod will take a couple of minutes to come up, as the Persistent Volume needs to be moved from one node to another.  The process should take a few minutes, not ten like the initial provisioning.  Again, when it is finally running:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb get pods
NAME                        READY     STATUS    RESTARTS   AGE
rethinkdb-111383062-9rp0f   1/1       Running   0          8m
```

The new agent that it is on can be found using the describe command again:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ kubectl --namespace=rethinkdb describe pod rethinkdb-111383062-9rp0f | grep Node
Node:		k8s-agent-ea300e7b-0/10.240.0.4
```

We see that it has moved to `k8s-agent-ea300e7b-0` when it was originally on `k8s-agent-ea300e7b-2`.

# Show the persisted data

Now that the pod has moved, let's show that the data is still there:

```bash
jims@azhat:~/src/github/acsbc_storage/app$ node ./index.js -h 13.64.114.4 -p 28015 -a get -t weather
[
  {
    "IATA": "SFO",
    "ICAO": "KSFO",
    "city": "San Francisco",
    "delay": "true",
    "id": "318306fd-efd6-47dd-b52f-c989bf0c15fe",
    "name": "San Francisco International",
    "state": "California",
    "status": {
      "avgDelay": "1 hour and 48 minutes",
      "closureBegin": "",
      "closureEnd": "",
      "endTime": "",
      "maxDelay": "",
      "minDelay": "",
      "reason": "WEATHER / LOW CEILINGS",
      "trend": "",
      "type": "Ground Delay"
    },
    "weather": {
      "meta": {
        "credit": "NOAA's National Weather Service",
        "updated": "5:56 PM Local",
        "url": "http://weather.gov/"
      },
      "temp": "63.0 F (17.2 C)",
      "visibility": 10,
      "weather": "Mostly Cloudy",
      "wind": "South at 13.8mph"
    }
  }
]
```
