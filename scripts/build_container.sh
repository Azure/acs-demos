#!/bin/bash

# This script builds a standalone SimDem container with one of the
# demo's installed.
#
# Usage:
#
# build_container NAME [SCRIPTS_DIR] [FLAVOR] 
#
# NAME is the Name of the container to be created.
#
# SCRIPTS_DIR defines the scripts directory to be copied into the
# container. 
#
# FLAVOR, if present, tells the script which container to build. Takes
# the value `novnc` or `cli`. If missing both flavors will be built

if [ -z "$1" ]; then
    # FIXME: create a usage description
    echo "You must provide a name for the image as the first parameter"
    exit 1
fi

if [ -z "$2" ]; then
    # FIXME: create a usage description
    echo "You must provide the name of a directory that contains the scripts to include in the container"
    exit 1
fi

REPOSITORY=rgardler
NAME_PREFIX=${1}
SCRIPTS_DIR=${2}
FLAVOR=${3:}

build_container() {
    IMAGE_NAME=${NAME_PREFIX}_$1
    docker build -t $IMAGE_NAME --build-arg SCRIPTS_DIR=$2 -f Dockerfile_$1 .
    
    if [ $? -eq 0 ]; then
	echo "Built $IMAGE_NAME containing scripts in $2"
    else
	echo "Failed to build $IMAGE_NAME containing scripts in $2"
	return 0
    fi
}

if [[ $FLAVOR == "novnc" ]]; then
    echo "FIXME: novnc does not work yet (fails authentication)"
    build_container novnc $SCRIPTS_DIR
elif [[ $FLAVOR == "cli" ]]; then
    build_container cli $SCRIPTS_DIR
else
    build_container cli $SCRIPTS_DIR
    if [ $? eq 1 ]; then
	exit 1
    fi
    build_container novnc $SCRIPTS_DIR
fi

exit $?
