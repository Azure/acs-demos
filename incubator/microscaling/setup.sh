#!/bin/sh
docker build -t rgardler/acs-logging-test-base .
docker build -t rgardler/acs-logging-test-simulate simulated_logging
docker build -t rgardler/acs-logging-test-web web
docker build -t rgardler/acs-logging-test-rest-enqueue rest-enqueue
docker build -t rgardler/acs-logging-test-rest-query rest-query
docker build -t rgardler/acs-logging-test-analyze analyze_logs
docker build -t rgardler/acs-logging-test-cli cli
docker build -t rgardler/acs-logging-test-microscaling microscaling
docker build -t rgardler/acs-logging-test-batch batch
docker build -t rgardler/acs-logging-test-master-proxy master-proxy
docker build -t rgardler/acs-logging-test-halo-pull halo_pull

