docker service update biglittlechallenge/trials-engine-arm

docker service update biglittlechallenge/trials-engine-dashboard-arm

docker service update create --name aiplayer --replicas=2 --network=trials --publish 8888 biglittlechallenge/trials-ai-arm

watch docker service ls
