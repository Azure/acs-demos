echo "#### building producer"
echo
docker build -t rgardler/acs-logging-test-rest-enqueue:test .

echo
echo "#### running producer"
echo
id=$(docker run -d -p 5000:5000 --env-file ../env.conf rgardler/acs-logging-test-rest-enqueue:test)
sleep 2
docker ps -l

echo
echo "#### check it's alive"
echo
curl http://localhost:5000/

echo
echo "#### enqueue a message"
echo
echo "BEFORE length"
echo
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length
curl -X POST -d queue=acsdemo -d message=messagetext http://localhost:5000/enqueue
echo
echo "AFTER length"
echo
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length

echo
echo "#### stop producer"
echo
docker stop $id

echo
echo "####  producer logs"
echo
docker logs $id
