# Ensure we are cleaning up the right cluster

In order to cleanup we will be deleting all the resources created in
the demo. Lets check the environment setup first to ensure we are
deleting the right resources.

```
cat ../env.local.json
```

If this isn't right press 'b' then 'CTRL-C', otherwise we will proceed...

```
az group delete --name $ACS_RESOURCE_GROUP --yes
sudo ssh-keygen -f "/root/.ssh/known_hosts" -R [${ACS_DNS_PREFIX}-${ACS_ID}mgmt.$ACS_REGION.cloudapp.azure.com]:2200
```
