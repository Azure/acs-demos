# Ensure we are connected to the DC/OS cluster

Here we will prepare a demo environment for managing DC/OS workloads.

## Preparation

We'll be using SSH, so lets ensure it is installed:

```
sudo apt-get install openssh-client -y
```

## Validate cluster

You can check that the cluster is available using the Azure CLI as
follows:

```
az acs show -g $ACS_RESOURCE_GROUP -n $ACS_CLUSTER_NAME --query provisioningState
```

Results:

```
"Succeeded"
```

If this says anything other than "Succeeded" or "Provisioning" you
will probably need to [cleanup](../delete_cluster/script.md) and try
redeploying the cluster. If it says "Provisioning" wait a little while
until you see "succeeded".

## Connect to the cluster

To connect to the DC/OS masters in ACS we need to open an SSH tunnel,
allowing us to view the DC/OS UI on our local machine.

```
ssh -NL 10000:localhost:80 -o StrictHostKeyChecking=no -p 2200 azureuser@${ACS_DNS_PREFIX}mgmt.${ACS_REGION}.cloudapp.azure.com &
```

NOTE: we supply the option `-o StrictHostKeyChecking=no` because we
want to be able to run these commands in an automated fashion for
demos. This option prevents SSH asking to validate the fingerprint. In
production one should always validate SSH connections.

