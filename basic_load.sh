#!/bin/bash

# Create a number of log event producers
# Create a single analyzer
# Monitor the queue (it will be growing)
# Create more analyzers
# The queue will start shrinking again

clear
echo "Starting two producers"
echo "======================================================================================="
echo ""
docker run -d -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue -e SIMULATION_ACTIONS=0 -e SIMULATION_DELAY=0 rgardler/acs-logging-test-simulate > /dev/null
docker run -d -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue -e SIMULATION_ACTIONS=0 -e SIMULATION_DELAY=0 rgardler/acs-logging-test-simulate > /dev/null
docker ps

echo ""
read -p "Press [Enter] key to start analyzing the data"
clear

echo "Starting a single analyzer"
echo "======================================================================================="
echo ""
docker run -d -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-analyze > /dev/null
docker ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear

echo "Output the status of the queue every 5 seconds"
echo "======================================================================================="
for i in {1..3}
do
    docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-cli
    sleep 5
    echo "======================================================================================="
    echo ""
done

echo ""
echo "Note how the queue length is growing"
read -p "Press [Enter] key to start 20 more analyzers"
clear

echo "Starting another 20 analyzers"
echo "======================================================================================="
for i in {1..20}
do
    docker run -d -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-analyze > /dev/null
done
docker ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear 

echo "Output the status of the queue every 5 seconds"
echo "======================================================================================="
for i in {1..5}
do
    docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-cli
    sleep 5
    echo "======================================================================================="
    echo ""
done

echo ""
echo "Note how the queue length is shrinking"
read -p "Press [Enter] key to kill all containers"
clear

echo "Stopping running containers"
echo "======================================================================================="
docker stop $(docker ps -q)

