#!/bin/bash

# Runs either a command line or headless VNC Docker container with
# Simdem installed.
#
# The container will be called `acs_demo_FLAVOR`
#
# Usage: run.sh [FLAVOR] [SCRIPT_DIR] [MODE]
#
# FLAVOR is an optional parameter to define which container to run,
# either the `novnc` or the `cli` versions. If not specified the
# `novnc` version is used
#
# SCRIPT_DIR is an optional parameter to define a directory containing
# SimDem scripts that will be mounted into the final container. Default
# value is `./demo_scripts`.
#
# MODE is an optional parameter that is only used by the CLI
# container, the novnc container will ignore it. MODE defines the mode
# of execution. It can take the values 'tutorial' (default, run with
# full instructional text), 'demo' (run with no instructional text and
# simulate typed commnds', 'test' (run in auto mode and test results
# against expected results)
#
# Connect with a browser at http://YOUR_DOCKER_HOST:8080/?password=vncpassword
#
# Based on https://github.com/ConSol/docker-headless-vnc-container


# Configuration options:
# You can configure the following variables to change behaviour of
# the container.
#
# VNC config
# ==========
VNC_COL_DEPTH='24'
VNC_RESOLUTION='1024x768'
VNC_PW='vncpassword'

VERSION=0.7.4-dev # The version of SimDem we have tested against.
FLAVOR=${1:-novnc}
SCRIPTS_DIR=${2:-`pwd`/demo_scripts}
MODE=${3:-tutorial}
REPOSITORY=rgardler
CONTAINER_NAME=simdem_$FLAVOR
SCRIPTS_VOLUME=${CONTAINER_NAME}_scripts
AZURE_VOLUME=azure_data
SSH_VOLUME=ssh_data

if [[ $FLAVOR == "novnc" ]]; then
    HOME="/headless"
else
    HOME="/home/simdem"
fi

echo Runing $REPOSITORY/$CONTAINER_NAME:$VERSION

echo Stopping and removing pre-existing containers
docker stop $CONTAINER_NAME
docker rm $CONTAINER_NAME
docker stop $SCRIPTS_VOLUME
docker rm $SCRIPTS_VOLUME

echo Creating scripts data container named $SCRIPTS_VOLUME containing the scripts in $SCRIPTS_DIR
docker create -v $HOME/demo_scripts --name $SCRIPTS_VOLUME ubuntu /bin/true
docker cp $SCRIPTS_DIR/. $SCRIPTS_VOLUME:$HOME/demo_scripts/

echo starting the $CONTAINER_NAME container in mode $MODE

if [[ $MODE == "tutorial" ]]; then
    COMMAND="run"
elif [[ $MODE == "demo" ]]; then
    COMMAND="run --style simulate"
elif [[ $MODE == "test" ]]; then
    COMMAND="test"
fi

if [[ $FLAVOR == "novnc" ]]; then
   docker run -d -p 5901:5901 -p 8080:6901 --name $CONTAINER_NAME \
       --volume $AZURE_VOLUME:$HOME/.azure \
       --volume $SSH_VOLUME:$HOME/.ssh \
       --volumes-from $SCRIPTS_VOLUME \
       -e VNC_COL_DEPTH=$VNC_COL_DEPTH \
       -e VNC_RESOLUTION=$VNC_RESOLUTION \
       -e VNC_PW=$VNC_PW \
       $REPOSITORY/$CONTAINER_NAME:$VERSION
else
    docker run -it \
       --volume $AZURE_VOLUME:$HOME/.azure \
       --volume $SSH_VOLUME:$HOME/.ssh \
       --volumes-from $SCRIPTS_VOLUME \
       --name $CONTAINER_NAME \
       $REPOSITORY/$CONTAINER_NAME:$VERSION \
       $COMMAND
fi
