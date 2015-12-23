A simple logging simulator used to test long running microservices on Azure Container Service.

This project consists of a number of Docker containers each desinged to perform a specific function. The main containers are:

  * logging_base - the container on which all other containers are based
  * simulate_logging - a container that simluates a period of logging activity, writes log items into the queue
  * analyze_logs - reads log queue and creates summary log data

# Running the tests

Build the containers as described below.

```
docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
docker run --volumes-from base --name simulate rgardler/acs-logging-test-simulate
docker run --volumes-from base --name analyze rgardler/acs-logging-test-analyze
docker run --volumes-from base --name cli rgardler/acs-logging-test-cli

```


# Building the containers

## Building the base container

```
docker build -t rgardler/acs-logging-test-base .
docker build -t rgardler/acs-logging-test-simulate simulated_logging
docker build -t rgardler/acs-logging-test-analyze analyze_logs
docker build -t rgardler/acs-logging-test-cli cli
```

