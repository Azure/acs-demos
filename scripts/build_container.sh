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
# SCRIPTS_DIR, if present, defines the scripts directory to be copied
# into the container. If ommitted the current directory will be copied
# in.
#
# FLAVOR, if present, tells the script which container to build. Takes
# the value `novnc` or `cli`. If missing both flavors will be built

REPOSITORY=rgardler
NAME_PREFIX=${1}
SCRIPTS_DIR=${2:.}
FLAVOR=${3:}

build_container() {
    IMAGE_NAME=${NAME_PREFIX}_$1
    docker build -t $IMAGE_NAME --build-arg SCRIPTS_DIR=$2 -f Dockerfile_novnc .
    
    if [ $? -eq 0 ]; then
	echo "Built $IMAGE_NAME containing scripts in $2"
    else
	echo "Failed to build $IMAGE_NAME containing scripts in $2"
	return 0
    fi
}

# FIXME: check we have a NAME_PREFIX

if [[ $FLAVOR == "novnc" ]]; then
    echo "FIXME: novnc does not work yet (fails authentication)"
    build_container novnc acs-engine
elif [[ $FLAVOR == "cli" ]]; then
    build_container cli acs-engine
else
    build_container cli acs-engine
    if [ $? eq 1 ]; then
	exit 1
    fi
    build_container novnc acs-engine
fi

exit $?
