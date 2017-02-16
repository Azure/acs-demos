# This is not a real test suite. It's more of a convenience when developing.

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

read -p "If the previous message is an HTML response from the server then it is listening on port 80. Hit return to close it down"

echo
echo "#### stop web container"
echo
docker stop $id

echo
echo "####  web container logs"
echo
docker logs $id
