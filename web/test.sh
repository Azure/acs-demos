echo "#### building web container"
echo
docker build -t rgardler/acs-logging-test-web:test .

echo
echo "#### running web container"
echo
id=$(docker run -d -p 8000:80 --env-file ../env.conf rgardler/acs-logging-test-web:test)
sleep 2
docker ps -l

echo
echo "#### check it's alive"
echo
curl http://localhost:8000

echo
echo "#### enqueue a message"
echo
echo "BEFORE length"
echo
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length
curl http://localhost:8000/send
echo
echo "AFTER length"
echo
docker run --env-file ../env.conf rgardler/acs-logging-test-cli length


echo
echo "#### stop web container"
echo
docker stop $id

echo
echo "####  web container logs"
echo
docker logs $id
