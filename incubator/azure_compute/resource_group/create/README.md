# Create a resource group

Here we will create a resource group to contain compute resources we
need to create.

```
az group create --name $SIMDEM_RESOURCE_GROUP --location $SIMDEM_RG_LOCATION
```

Results:

```expected_similarity=0.3
{
  "id": "/subscriptions/325e7c34-99fb-4190-aa87-1df746c67705/resourceGroups/test",
  "location": "westus",
  "managedBy": null,
  "name": "test",
  "properties": {
	"provisioningState": "Succeeded"
  },
  "tags": null
}
```

# Validation

```
az group list --query "[?name=='$SIMDEM_RESOURCE_GROUP']"
```

Results:

```expected_similarity=0.3
{
  "id": "/subscriptions/325e7c34-99fb-4190-aa87-1df746c67705/resourceGroups/test",
    "location": "westus",
	"managedBy": null,
	"name": "test",
	"properties": {
		"provisioningState": "Succeeded"
	},
	"tags": null
}
```