#!/bin/bash

# Check SimDem is installed, and if it is run it with the supplied parameters.

if hash simdem 2>/dev/null; then
    echo "Simdem version: `simdem --version`"
else
    echo "This demo is run with Simdem. however, SimDem is not installed. See http://github.com/rgardler/simdem \n"
    echo "You can still read the tuturial markdown files and manual run the demo."
    exit 1
fi;

if [ $# -eq 0 ]; then
    COMMAND_AND_OPTIONS="run --path ."
else
    COMMAND_AND_OPTIONS=$@
fi

if [ ! -f /.dockerenv ]; then
    sudo simdem $COMMAND_AND_OPTIONS
else
    simdem $COMMAND_AND_OPTIONS
fi
