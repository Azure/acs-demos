#!/bin/sh
docker build -t rgardler/acs-logging-test-base .
docker build -t rgardler/acs-logging-test-simulate simulated_logging
docker build -t rgardler/acs-logging-test-analyze analyze_logs
docker build -t rgardler/acs-logging-test-cli cli

docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
docker run --volumes-from base rgardler/acs-logging-test-simulate
docker run --volumes-from base rgardler/acs-logging-test-analyze
docker run --volumes-from base rgardler/acs-logging-test-cli
