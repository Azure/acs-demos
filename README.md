A simple logging simulator used to test long running microservices on Azure Container Service.

This project consists of a number of Docker containers each desinged to perform a specific function. The main containers are:

  * logging_base - the container on which all other containers are based
  * simulate_logging - a container that simluates a period of logging activity, writes log items into the queue
  * rest_enqueue - provides a REST API for adding messages to the queue
  * analyze_logs - reads log queue and creates summary log data
  * cli - a simple CLI tool for working with the data produced by the simulateion and he analyzer

At present it uses Azure Storage Queues and Tables.

## Setup

Create a storage account for your application to use. Then copy
`env.conf.tmpl` to `env.conf` and add values for the following
parameters:

```
AZURE_STORAGE_QUEUE_NAME
AZURE_STORAGE_ACCOUNT_NAME
AZURE_STORAGE_ACCOUNT_KEY
```

Now run 'setup.sh', this will build all the containers and publish
them to Docker Hub.

## Running the application

### REST API

To run the REST endpoint:

`docker run -d -p 5000:5000 --env-file env.conf rgardler/acs-logging-test-rest-enqueue`

To post messages to this endpoint:

POST to `http://localhost:5000/enqueue' with a a form data payload as
follows: 

`queue=<queue_name>&message=<message_text>`

For example:

```
curl -X POST -d queue=acsdemo -d message=messagetext http://localhost:5000/enqueue
```

For more information see the README in the [`rest_enqueue`](rest_enqueue) folder.

### Simulation

Run `autoscale.sh` for a scripted demo of the application, showing the
scaling of the analyzer container.

You can run the application with `docker-compose up -d`, this will
start a simulated environment in which the temperature of a room is
being controlled. It will throw errors if too hot or cold, warnings if
a little warm or cold and info if just right. The analyzer in this
demo counts the number of each type of message and stores the results
in the table.

You can scale components with commands such as `docker-compose scale
analyzer=5` or `docker-compose scale producer=2`

## Command Line Interface

The CLI container provides basic tooling for interacting with the
application. It is run with:

```bash
docker run --env-file env.conf cli COMMAND
```

Where COMMAND is replaced with one of the following available commands:

### summary

Prints a summary of the current status of the logging application, for example:

```
Queue Length is approximately: 0

Processed events:
Errors: 3941
Warnings: 608
Infos: 5803
Debugs: 0
Others: 0
```

### length

Outputs the approximate length of the queue.

### createQueue

Create the queue. It will automatically be recreted if you restart a
producer. This is provided for testing purposes only.

### deleteQueue

Delete the queue. It will automatically be recreted if you restart a
producer.

### createTable

Create the table. It will automatically be recreted if you restart a
producer. This is provided for testing purposes only.

### deleteTable

Delete the table. It will automatically be recreted if you restart a
producer.


## Slack Channel

Messages from the test suite are sent to the Slack channel
https://azurecontainerservice.slack.com, this is configurable in
config.py (see below)

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

Alternatively just use the second line as a cron job and use one or more long running task for the first, such as:

docker -H :2375 run -d --restart="on-failure:5" -e SMTP_PASSWORD=password -e SIMULATION_ACTIONS=0 --volumes-from base rgardler/acs-logging-test-simulate

# Development

Run your chosen container in interactive mode:

```
docker run -e SMTP_PASSWORD=password --rm -it rgardler/acs-logging-test-simulate bash
```

Now you can run commands to test things out.
