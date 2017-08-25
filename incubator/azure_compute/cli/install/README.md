# Install the Azure CLI

In this section we will install (or upgrade) the Azure CLI.

```
echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ wheezy main" | sudo tee /etc/apt/sources.list.d/azure-cli.list
sudo apt-key adv --keyserver packages.microsoft.com --recv-keys 417A0893
sudo apt-get install apt-transport-https
sudo apt-get update
sudo apt-get install azure-cli
```

# Validation

```
az --version
```

Results:

```
azure-cli (2.0.14)

acr (2.0.10)
acs (2.0.13)
appservice (0.1.13)
batch (3.1.1)
billing (0.1.3)
cdn (0.0.6)
cloud (2.0.7)
cognitiveservices (0.1.6)
command-modules-nspkg (2.0.1)
component (2.0.7)
configure (2.0.10)
consumption (0.1.3)
container (0.1.8)
core (2.0.13)
cosmosdb (0.1.11)
dla (0.0.10)
dls (0.0.12)
eventgrid (0.1.2)
feedback (2.0.6)
find (0.2.6)
interactive (0.3.7)
iot (0.1.10)
keyvault (2.0.8)
lab (0.0.9)
monitor (0.0.8)
network (2.0.12)
nspkg (3.0.1)
profile (2.0.10)
rdbms (0.0.5)
redis (0.2.7)
resource (2.0.12)
role (2.0.10)
sf (1.0.6)
sql (2.0.9)
storage (2.0.12)
vm (2.0.12)

Python (Linux) 3.6.1 (default, Aug 17 2017, 10:48:15)
[GCC 4.8.4]

Python location '/opt/az/bin/python3'
```
