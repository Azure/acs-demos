# Remove Resource Group

Since everything in this demo was created in a singel resource group
the easiest way to cleanup is to delete that resource group.

```
az group delete --name $SIMDEM_RESOURCE_GROUP_NAME --yes
```

# Clean up SSH hosts file

Since we added a (now deleted) SSH host we should clean up our SSH
hosts file to avoid warnings about possible spoofing should we
recreate the machine with the same DNS.

```
sudo ssh-keygen -f "/root/.ssh/known_hosts" -R [${SIMDEM_DNS_PREFIX}mgmt.$SIMDEM_RESOURCE_GROUP_LOCATION.cloudapp.azure.com]:2200
```
