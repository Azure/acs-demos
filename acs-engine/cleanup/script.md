# Cleanup after ACS Engine demos / tutorials

## Delete Service Principle

```
az ad sp delete --id http://$ACSE_SERVICE_PRINCIPLE_NAME
```

Results:

```
Removing role assignments
```

## Delete the resource group

```
az group delete --name $ACSE_RESOURCE_GROUP --yes
```

## Delete the workspace

```
rm -rf $ACSE_WORKSPACE
```
