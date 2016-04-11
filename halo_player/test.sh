COMPOSE_FILE=docker-compose.yml

echo "#### building"
echo
docker-compose -f $COMPOSE_FILE build

echo "#### running"
docker-compose up
