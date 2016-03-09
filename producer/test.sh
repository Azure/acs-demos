echo "#### building producer"
docker build -t rgardler/acs-logging-test-producer:test .

echo "#### running producer"
id=$(docker run -d -p 5000:5000 --env-file ../env.conf rgardler/acs-logging-test-producer:test)
sleep 2
docker ps -l

echo "#### check it's alive"
curl http://localhost:5000/

echo "#### enqueue a message"
curl -X PUT -d queue=testQueue -d message=messagetext http://localhost:5000/enqueue

echo "#### stop producer"
docker stop $id

echo "####  producer logs"
docker logs $id
