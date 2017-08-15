# Logging into Azure using the Azure CLI

There are two ways of logging into the Azure CLI tool. One is to use
the interactive `azure login` command, the other (which we use here)
is to have a Service Principle.

# Prerequisites

We need to create a service principle to use for this login.

# Login

With our service principle we can now login to Azure:

```
az login --service-principal --username ${SERVICE_PRINCIPAL_NAME} --password ${SERVICE_PRINCIPAL_SECRET_KEY} --tenant ${TENNANT_ID} --output=table
```

Results:

```expected_similarity=0.4
EnvironmentName    IsDefault    Name                           State    TenantId
-----------------  -----------  -----------------------------  -------  ------------------------------------
AzureCloud         True         Azure Container Service Demos  Enabled  72f988bf-86f1-41af-91ab-2d7cd011db47
```

# Validation

Ensure we are logged in with the Azure CLI.

```
az account show --output=table
```

Results:

```expected_similarity=0.4
EnvironmentName    IsDefault    Name                           State    TenantId
-----------------  -----------  -----------------------------  -------  ------------------------------------
AzureCloud         True         Azure Container Service Demos  Enabled  72f988bf-86f1-41af-91ab-2d7cd011db47
```

