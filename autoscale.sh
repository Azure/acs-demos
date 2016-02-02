#!/bin/bash

# Demonstrate the scaling up and down of analysers in response to the
# length of the queue.

clear
echo "Starting one producer and one analyzer"
echo "======================================================================================="
echo ""
docker-compose scale producer=1
docker-compose scale analyzer=1
docker-compose up -d 
docker-compose ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear

echo "Output the status of the queue every 5 seconds"
echo "======================================================================================="
for i in {1..3}
do
    docker run -it rgardler/acs-logging-test-cli summary
    echo ""
    docker-compose ps
    echo "======================================================================================="
    echo ""
    sleep 5
done

echo "Notice how a number of the analyzers have stopped (queue length went to 0)"
echo "But, queue is starting to grow again"
read -p "Press [Enter] key to turn on an auto-scaling algorithm"
clear 

for i in {1..10}
do
    length=$(docker run -i rgardler/acs-logging-test-cli length)

    echo ""

    if [ "$length" -gt 50 ]
    then
	echo "Queue is too long ($length)"
	docker-compose scale analyzer=10
    else 
	echo "Queue is an acceptable length ($length)"
    fi
    docker-compose ps
    echo "======================================================================================="
    echo ""
    sleep 5
done


read -p "Press [Enter] key to shut things down"
clear 

docker-compose stop
