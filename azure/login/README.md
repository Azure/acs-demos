# Azure Login

There are two ways to login using the Azure CLI. The first one
requires you to submit a code into a browser authentication page, the
second requires you to set-up a Service Principle. While Service
Principles are significantly more flexible, the interactive login
is much easier, so we will focus on that for now.

## Device Login

The following command will start the login procedure. It will display
a code that needs to be typed into a form at
`http://aka/ms/devicelogin`. Once you authenticate in that web
application the login will proceed.

```
az login &
xdg-open http://aka.ms/devicelogin
fg
```

NOTE: the reason for running in the background and bringing it
immediately to the foreground is to work around a limitation with the
SimDem tool this document is used in, you can ignore that if you wish.


# Validation

You will need an active azure subscription. Before proceeding with
this script ensure that you are logged in using `az login`.

```
az account show --output=table
```

Results:

```expected_similarity=0.4
EnvironmentName    IsDefault    Name                           State    TenantId
-----------------  -----------  -----------------------------  -------  ------------------------------------
AzureCloud         True         Azure Container Service Demos  Enabled  72f988bf-86f1-41af-91ab-2d7cd011db47
```
