# Azure Login

There are two ways to login using the Azure CLI. The first one
requires you to submit a code into a browser authentication page, the
second requires you to set-up a Service Principle. While Service
Principles are significantly more flexible, the interactive login
is much easier, so we will focus on that for now.

## Device Login

The `az login` command will start the login procedure. It will display
a code that needs to be typed into a form at
`http://aka/ms/devicelogin`. Once you authenticate in that web
application the login will proceed.

# Logging into Azure using a Service Principles

There are two ways of logging into the Azure CLI tool. One is to use
the interactive `azure login` command, the other (which we use here)
is to have a Service Principle.

## Getting a Service Principle

We need to create a service principle to use for this login.

## Login

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
