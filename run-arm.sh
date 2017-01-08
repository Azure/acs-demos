docker network create --driver overlay --subnet 20.0.14.0/24 trials

docker service create --replicas=1 --network=trials --publish 8080:8080 --name engine --env MIN_NUMBER_OF_PLAYERS=5 biglittlechallenge/trials-engine-arm

docker service create --name aiplayer --replicas=2 --network=trials --publish 8888 biglittlechallenge/trials-ai-arm

echo "use 'docker service scale aiplayer=5' to add more players"
