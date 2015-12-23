#!/bin/sh
docker create -v /logs --name base rgardler/acs-logging-test-base /bin/true
docker run --volumes-from base --name simulate rgardler/acs-logging-test-simulate
docker run --volumes-from base --name analyze rgardler/acs-logging-test-analyze
docker run --volumes-from base --name cli rgardler/acs-logging-test-cli
