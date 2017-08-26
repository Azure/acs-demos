# Cleanup after ACS Engine demos / tutorials

# Prerequisite

You will need an active Azure subscription and you will need to have
the Azure CLI installed. You'll also need to
be [logged in to Azure](../../azure/login/README.md).

## Delete Service Principle

```
az ad sp delete --id http://$SIMDEM_ACSE_SERVICE_PRINCIPLE_NAME
```

Results:

```
Removing role assignments
```

## Delete the resource group

```
az group delete --name $SIMDEM_RESOURCE_GROUP --yes
```

## Delete the workspace

```
rm -rf $SIMDEM_ACSE_WORKSPACE
```
