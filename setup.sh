#!/bin/sh
docker build -t rgardler/acs-logging-test-base .
docker build -t rgardler/acs-logging-test-simulate simulated_logging
docker build -t rgardler/acs-logging-test-analyze analyze_logs
docker build -t rgardler/acs-logging-test-cli cli

# If using files for queue
# docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
# docker run -e ACS_LOGGING_QUEUE_TYPE=LocalFile --volumes-from base rgardler/acs-logging-test-simulate
# docker run -e ACS_LOGGING_QUEUE_TYPE=LocalFile --volumes-from base rgardler/acs-logging-test-analyze
# docker run -e ACS_LOGGING_QUEUE_TYPE=LocalFile --volumes-from base rgardler/acs-logging-test-cli

# If using Azure queue
docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue -e SIMULATION_DELAY=0 rgardler/acs-logging-test-simulate
docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-analyze
docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-cli summary
