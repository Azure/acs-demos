# Create a Cluster

```
az login
```

```
az group create -n rgdcosvamp -l eastus2
```

```
az acs create -n rgdcosvamp -g rgdcosvamp -d rgdcosvamp --generate-ssh-keys
```

# Install DC/OS CLI

```
sudo az acs dcos install-cli
```

```
dcos config set core.dcos_url http://localhost
```


# Connect to the cluster

```
sudo ssh -fNL 80:localhost:80 -p 2200 azureuser@rgdcosvampmgmt.eastus2.cloudapp.azure.com -i ~/.ssh/id_rsa
```

FIXME: this should work but doesn't in my case

```
az acs dcos browse -n rgdcosvamp -g rgdcosvamp
```

# Deploy Elastic Search

Using marathon application definition file `elasticsearch.json` we
will deploy Elastic Search to our cluster. Lets take a look at that
file. We should note that this configuration of Elastic Search is not
intended for production deployments, it's a quick and simple
installation for this demo/tutorial.

```
cat elasticsearch.json
```

To deploy the application we can use the web UI, but here we will use
the CLI:

```
dcos marathon app add elasticsearch.json
```


# Deploy Vamp

```
dcos package install vamp --options vamp.json --yes
```




# Visit the Vamp UI

http://localhost/service/vamp
