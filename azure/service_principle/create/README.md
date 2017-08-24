# Create a Service Principal

A service principal is required in some cicumstances to enable an
application to manipulate your Azure resources. You can create one
using the az CLI.

# Prerequisites

You will need to be [logged in to you Azure subscription](../../login).

You will also need a [Resource Group](../../../incubator/azure_compute/resource_group/create/) upon which this service principal will operate.

# Create the Service Principal with Azure CLI

Use the Azure CLI to create a Service Principal that can perform
operations on your resource group.

First we need out Subscription ID:

```
SIMDEM_SUBSCRIPTION_ID=$(az account show | jq -r '.id')
```

Next up we create the service principle:

```
az ad sp create-for-rbac --name $SIMDEM_SERVICE_PRINCIPAL_NAME --role="Contributor" --scopes="/subscriptions/$SIMDEM_SUBSCRIPTION_ID/resourceGroups/$SIMDEM_RESOURCE_GROUP"
```

Results:

```expected_similarity=0.2
{
  "appId": "657cd27f-xxxx-xxxx-xxxx-512adb26e468",
  "displayName": "simdem",
  "name": "http://simdem",
  "password": "51d6a411-xxxx-xxxx-xxxx-974053b1a80a",
  "tenant": "72f988bf-xxxx-xxxx-xxxx-2d7cd011db47"
}
```

# Validation

We can test wheter the service principle already exists using the
CLI. 

```
az ad sp show --id http://$SIMDEM_SERVICE_PRINCIPAL_NAME
```

Results:

```expected_similarity=0.4
{
  "appId": "e1a19126-8d39-4a37-8f43-15169fc8e530",
  "displayName": "simdem_sp",
  "objectId": "cc029b17-1d25-4774-8d83-6e18f4ad456d",
  "objectType": "ServicePrincipal",
  "servicePrincipalNames": [
	"http://simdem_sp",
	"e1a19126-8d39-4a37-8f43-15169fc8e530"
	]
}					
```

To make it easier to reuse this SP later we will create
environment variables for reuse:

```
SIMDEM_APP_ID=$(az ad sp show --id http://$SIMDEM_SERVICE_PRINCIPAL_NAME --query appId --output tsv)
echo "App ID is $SIMDEM_APP_ID"
```

Results:

```expected_similarity=0.2
App ID is 1e3edf6e-XXXX-XXXX-XXXX-7c785fb3ad95
```

```
SIMDEM_TENANT_ID=$(az account show --output tsv --query tenantId)
```
