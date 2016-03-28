echo "#### building web container"
echo
docker build -t rgardler/acs-logging-test-web:test .

echo
echo "#### running web container"
echo
id=$(docker run -d -p 80:80 --env-file ../env.conf rgardler/acs-logging-test-web:test)
sleep 2
docker ps -l

echo
echo "#### check it's alive"
echo
curl http://localhost

echo
echo "#### stop web container"
echo
docker stop $id

echo
echo "####  web container logs"
echo
docker logs $id
