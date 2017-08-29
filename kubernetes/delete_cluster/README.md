# Delete the Kubernetes resource group

In order to cleanup we will be deleting all the resources created in
the demo. 

# Prerequisites

You will need a to
be [logged in to Azure](../../azure/login/README.md).


# Delete the resource group

```
az group delete --name $SIMDEM_RESOURCE_GROUP --yes
```

# All done

All resources have now been deleted.


