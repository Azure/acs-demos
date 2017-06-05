#!/bin/bash

# Create or restart and connect to an instance of SimDem

if [ $# -eq 0 ]; then
    COMMAND_AND_OPTIONS="run ."
else
    COMMAND_AND_OPTIONS=$@
fi

IMAGE_NAME=rgardler/simdem
IMAGE_VERSION=latest
CONTAINER_NAME=simdem

RUNNING=$(docker inspect -f {{.State.Running}} $CONTAINER_NAME 2> /dev/null)

if [ "$?" -ne "0" ]
then
    # echo "Executing a '$CONTAINER_NAME' container"
    docker run -it -v ~/.ssh:/root/.ssh -v ~/.azure:/root/.azure -v $(pwd):/demo_scripts --name $CONTAINER_NAME $IMAGE_NAME:$IMAGE_VERSION $COMMAND_AND_OPTIONS
    exit 0
fi

if [ "$RUNNING" == "true" ]
then
    # echo "Connecting to running '$CONTAINER_NAME' container"
    docker exec -it $CONTAINER_NAME python3 run.py $COMMAND_AND_OPTIONS
else
    # echo "Re-starting and connecting to an '$CONTAINER_NAME' container"
    docker start $CONTAINER_NAME
    docker exec -it $CONTAINER_NAME python3 run.py $COMMAND_AND_OPTIONS
fi
