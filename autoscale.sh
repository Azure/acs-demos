#!/bin/bash

# Demonstrate the scaling up and down of analysers in response to the
# length of the queue.

SCALE_SET_NAME=swarm-agent-94077C7vmss-0

PRODUCERS=1
MAX_PRODUCERS=20
ANALYZERS=1
MAX_ANALYZERS=100

STATUS_REPEATS=3
STATUS_DELAY=5

CONTAINER_SCALE_REPEATS=5
CONTAINER_SCALE_DELAY=5

clear
echo "Starting $PRODUCERS producer and $ANALYZERS analyzer"
echo "======================================================================================="
echo ""
docker-compose scale producer=$PRODUCERS
docker-compose scale analyzer=$ANALYZERS
docker-compose up -d 
docker-compose ps

echo ""
read -p "Press [Enter] key to see the effect on the queue"
clear

echo "Output the status of the queue every $STATUS_DELAY seconds"
echo "======================================================================================="
for i in $(seq "$STATUS_REPEATS")
do
    docker run -it rgardler/acs-logging-test-cli summary
    echo ""
    docker-compose ps
    echo "======================================================================================="
    echo ""
    sleep $STATUS_DELAY
done

echo "Notice how the queue is starting to grow again"
read -p "Press [Enter] key to turn on an auto-scaling algorithm"
clear 

for i in $(seq "$CONTAINER_SCALE_REPEATS")
do
    LENGTH=$(docker run -i rgardler/acs-logging-test-cli length)

    echo ""

    if [ "$LENGTH" -gt 50 ]
    then
	echo "Queue is too long ($LENGTH)"
	NUM_ANALYZERS=expr $LENGTH / 100
	if [ "$LENGTH" -gt "$MAX_ANALYZERS" ]
	then
	    case  in
	    esac
	    
	    NUM_ANALYZERS=$MAX_ANALYZERS
	fi
	echo "Scaling to $NUM_ANALYZERS"
	docker-compose scale analyzer=$NUM_ANALYZERS
    else 
	echo "Queue is an acceptable length ($length)"
    fi
    docker-compose ps
    echo "======================================================================================="
    echo ""
    sleep $CONTAINER_SCALE_DELAY
done

echo "That's all for our demo just now..."
read -p "Press [Enter] key to shut things down"
clear 

docker-compose stop
