A simple logging simulator used to test long running microservices on Azure Container Service.

This project consists of a number of Docker containers each desinged to perform a specific function. The main containers are:

  * logging_base - the container on which all other containers are based
  * simulate_logging - a container that simluates a period of logging activity, writes log items into the queue
  * analyze_logs - reads log queue and creates summary log data

## Slack Channel

Messages from the test suite are sent to the Slack channel https://azurecontainerservice.slack.com

# Running the tests

Copy src/config_tmpl.py to src/config.py and edit accordingly. 

Build the containers with:

```
docker build -t rgardler/acs-logging-test-base .
docker build -t rgardler/acs-logging-test-simulate simulated_logging
docker build -t rgardler/acs-logging-test-analyze analyze_logs
docker build -t rgardler/acs-logging-test-cli cli
```

Ensure that the volume is created:

```
docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
```

Simulate some test data (you might want to run this on a schedule):

```
docker run --rm --volumes-from base --name simulate rgardler/acs-logging-test-simulate
```

Analyze the log data in the queue:

```
docker run --rm --volumes-from base --name analyze rgardler/acs-logging-test-analyze
```

To get some insight into what has happened you can run the CLI container:

```
docker run -it --rm --volumes-from base --name cli rgardler/acs-logging-test-cli
```

# Automating the tests

Run the setup script to ensure the containers are correctly installed:

```
setup.sh
```

Now add the following lines to your crontab:

```
*/5 * * * * docker -H :2375 run --rm --volumes-from base rgardler/acs-logging-test-simulate
*/22 * * * * docker -H :2375 run --rm --volumes-from base rgardler/acs-logging-test-analyze
```

# Development

Run your chosen container in interactive mode:

```
docker run --rm -it rgardler/acs-logging-test-simulate bash
```

Now you can run commands to test things out.
