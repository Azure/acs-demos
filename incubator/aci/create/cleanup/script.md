# Cleanup all resources used in the ACI demos

Check we are cleaning up the right resources:

```
env | grep SIMDEM_.*
```

```
az group delete --name $SIMDEM_RESOURCE_GROUP --yes
```
