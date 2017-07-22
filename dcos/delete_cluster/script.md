# Cleanup an ACS DC/OS cluster

Here we will remove the DC/OS demo cluster from Azure. Note that this
deletes all resources so please use with care.

## Ensure we are cleaning up the right cluster

In order to cleanup we will be deleting all the resources created in
the demo. Lets check the environment setup first to ensure we are
deleting the right resources.

```
env | grep ACS_.*
```

If this isn't right press 'b' then 'CTRL-C', otherwise we will proceed...

```
az group delete --name $ACS_RESOURCE_GROUP --yes
ssh-keygen -R [${ACS_DNS_PREFIX}mgmt.$ACS_REGION.cloudapp.azure.com]:2200
```
