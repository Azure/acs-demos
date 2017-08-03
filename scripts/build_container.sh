#!/bin/bash

# This script builds a standalone SimDem container with one of the
# demo's installed.

docker build -t simdem_test --build-arg SCRIPTS_DIR=acs-engine .
