# Running non-container tasks

This spike runs a simple task on a Linux agent using DC/OS

# Preparation

Troughout this work we will be using environment variables to
described te cluster we will be using. These can be viewed with the
following command:

```
env | grep ACS_.*
```

## Prerequisites

We first need to [create a DC/OS cluster](../../../../dcos/create_cluster/script.md), if you haven't done so yet, please do so no.

We will also need to [connect to the cluster](../../../../dcos/connect_to_cluster/script.md) so that we can manage it.


## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com &
sleep 3 # This sleep is to ensure that SSH connects before we move to the next stage
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

## Ensure no previous tests are running

In case we have any of the applications this script creates running
from a previous run we will attempt to remove them now. Don't worry if
these commands report an application is not found.

```
dcos marathon app remove --force /test/sleep
```

# Test scheduling a simple application

First we will try scheduling a simple application, in this case it
will sleep for a long time. We define this task in a Marathon.json
file:

```
cat $SIMDEM_CWD/sleep_app.json
```

Results:

```
{
   "id": "/test/sleep",
   "cmd": "sleep 100000000",
   "cpus": 1,
   "instances": 1
}
```

To schedule this task we use the `dcos` command line too:

```
dcos marathon app add $SIMDEM_CWD/sleep_app.json
```

Results:

```expected_similarity=0.3
Created deployment bd64fbd2-dbfc-469d-9b68-42573dea8378
```

## Checking the status of the application

We can check the state of the app:

```
dcos marathon app list
```

Results:

```
ID           MEM  CPUS  TASKS  HEALTH  DEPLOYMENT  WAITING  CONTAINER  CMD
/test/sleep  128   1     1/1    ---       ---      False      mesos    sleep 100000000
```




