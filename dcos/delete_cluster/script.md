# Cleanup an ACS DC/OS cluster

Here we will remove the DC/OS demo cluster from Azure. Note that this
deletes all resources so please use with care.

# Prerequisite

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll need to
be [logged in to Azure](../../azure/login/README.md).

# Delete the resources

```
az group delete --name $SIMDEM_RESOURCE_GROUP --yes
ssh-keygen -R [${SIMDEM_DNS_PREFIX}mgmt.$SIMDEM_LOCATION.cloudapp.azure.com]:2200
```
