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
docker run --rm -e SMTP_PASSWORD=password --volumes-from base --name simulate rgardler/acs-logging-test-simulate
```

You can configure the simulattion with the following environment variables (which can be passed in the docker run commadn using `-e`):

SIMULATION_ACTIONS: the number of actions to be simulated on this run (0 means continue until stopped)

SIMULATION_DELAY: the delay between simulated events


Analyze the log data in the queue:

```
docker run --rm -e SMTP_PASSWORD=password --volumes-from base --name analyze rgardler/acs-logging-test-analyze
```

To get some insight into what has happened you can run the CLI container:

```
docker run -it -e SMTP_PASSWORD=password --rm --volumes-from base --name cli rgardler/acs-logging-test-cli
```

# Automating the tests

Ensure DOCKER_HOST is set to your Swarm endpoint.

Ensure that the volume is created:

```
docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
```

Now add lines such as the following to the cronteb (use `crontab -e`):

```
*/5 * * * * docker -H :2375 run -e SMTP_PASSWORD=password --rm --volumes-from base rgardler/acs-logging-test-simulate
*/22 * * * * docker -H :2375 run -e SMTP_PASSWORD=password --rm --volumes-from base rgardler/acs-logging-test-analyze
```

Alternatively just use the second line as a cron job and use a long running task for the first, such as:

docker -H :2375 run -d -e SMTP_PASSWORD=password -e SIMULATION_ACTIONS=0 --rm --volumes-from base rgardler/acs-logging-test-simulate

# Development

Run your chosen container in interactive mode:

```
docker run -e SMTP_PASSWORD=password --rm -it rgardler/acs-logging-test-simulate bash
```

Now you can run commands to test things out.
