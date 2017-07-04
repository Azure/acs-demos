# Ensure we are cleaning up the right cluster

In order to cleanup we will be deleting all the resources created in
the demo. Lets check the environment setup first to ensure we are
deleting the right resources.

```
echo "Delete resource group '$ACS_RESOURCE_GROUP', containing '$ACS_CLUSTER_NAME'"
```

If this isn't right press 'b' then 'CTRL-C', otherwise we will proceed...

```
az group delete --name $ACS_RESOURCE_GROUP --yes
sudo ssh-keygen -f "~/.ssh/known_hosts" -R [${ACS_DNS_PREFIX}mgmt.$ACS_REGION.cloudapp.azure.com]:2200
```

Now we will remove the kubernetes source we checked out:

```
rm -Rf ~/.acs/demo/kubernetes
```

# All done

All resources have now been deleted.


