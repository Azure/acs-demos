echo Creating Network
docker network create --driver overlay trials
sleep 5

echo
echo Creating engine service
docker service create --replicas=1 --hostname=engine --network=trials --publish 8080:8080 --name engine --env MIN_NUMBER_OF_PLAYERS=5 biglittlechallenge/trials-engine-arm
sleep 5 # ensure that DNS updates have completed before we start other services

echo
echo Creating engine-dashboard
docker service create --replicas=1 --network=trials --publish 8181:80 --name engine-dashboard biglittlechallenge/trials-engine-dashboard-arm

echo
echo Creating aiplayer service
docker service create --name aiplayer --replicas=2 --network=trials --publish 8888 biglittlechallenge/trials-ai-arm

echo
docker service ls

echo "use 'docker service scale aiplayer=5' to add more players"
