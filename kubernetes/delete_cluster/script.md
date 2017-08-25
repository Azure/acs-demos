# Ensure we are cleaning up the right cluster

In order to cleanup we will be deleting all the resources created in
the demo. Lets check the environment setup first to ensure we are
deleting the right resources.

```
echo "Delete resource group '$SIMDEM_RESOURCE_GROUP', containing '$SIMDEM_CLUSTER_NAME'"
```

If this isn't right press 'b' then 'CTRL-C', otherwise we will proceed...

# Delete the resource group

```
az group delete --name $SIMDEM_RESOURCE_GROUP --yes
```

# All done

All resources have now been deleted.


