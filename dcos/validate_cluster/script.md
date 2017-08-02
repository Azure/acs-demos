# Validate that a DC/OS is availble and working correctly

This tutorial will help you ensure that your DC/OS cluster is working
correctly.

## Preparation

The cluster configuration we will validate here is defined in the
following enviornment variables:


``` 
env | grep ACS_.* 
```

## Check ACS cluster exists


You can check that the cluster is available using the Azure CLI as
follows:

```
az acs show -g $ACS_RESOURCE_GROUP -n $ACS_CLUSTER_NAME --query provisioningState
```

Results:

```
"Succeeded"
```

If this says anything other than "succeeded" (including an empty
response) you will need to [cleanup](../delete_cluster/script.md) and
try [redeploying the cluster](../create_cluster/script.md). If it says
"Provisioning" wait a little longer before proceeding.

