COMPOSE_FILE=docker-compose.yml

echo "#### building"
echo
docker-compose -f $COMPOSE_FILE build

echo "#### running"
echo "BEFORE length"
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length
echo
docker-compose up
echo "AFTER length"
echo
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length
