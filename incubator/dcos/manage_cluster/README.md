# Manage a DC/OS cluster

In order to manage a DC/OS cluster we must connect to it using SSH.

## Prerequisites 

Not surprisingly, we will need a DC/OS cluster up and
running, if you don't yet have one then work
through [creating a DC/OS cluster on ACS](https://raw.githubusercontent.com/Azure/acs-demos/master/dcos/create_cluster/README.md). 

## Validate cluster

We can quickly check if the cluster it up and running using the Azure
CLI as follows:

```
az acs show -g $SIMDEM_RESOURCE_GROUP -n $SIMDEM_CLUSTER_NAME --query provisioningState
```

Results:

```
"Succeeded"
```

If this says "Failed" or "error" you will need
to [cleanup](../delete_cluster/README.md) and try redeploying the
cluster. If it says "Provisioning" wait a little longer before
proceeding.

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
sudo apt-get install openssh-client -y
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${SIMDEM_DNS_PREFIX}mgmt.${SIMDEM_LOCATION}.cloudapp.azure.com &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

