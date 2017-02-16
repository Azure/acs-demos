#!/bin/bash

# Create a number of log event producers
# Create a single analyzer
# Monitor the queue (it will be growing)
# Create more analyzers
# The queue will start shrinking again

clear
echo "Starting two producers and one analyzer"
echo "======================================================================================="
echo ""
docker-compose scale producer=2
docker-compose scale analyzer=1
docker-compose up -d 
docker ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear

echo "Output the status of the queue every 5 seconds"
echo "======================================================================================="
for i in {1..3}
do
    docker run -e ACS_LOGGING_QUEUE_TYPE=AzureStorageQueue rgardler/acs-logging-test-cli summary
    sleep 5
    echo "======================================================================================="
    echo ""
done

echo ""
echo "Note how the queue length is growing"
read -p "Press [Enter] key to start 20 more analyzers"
clear

echo "Scale up to 20 analyzers"
echo "======================================================================================="
docker-compose scale analyzer=20
docker ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear 

echo "Output the status of the queue every 5 seconds"
echo "======================================================================================="
for i in {1..5}
do
    docker run rgardler/acs-logging-test-cli summary
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
docker-compose stop

