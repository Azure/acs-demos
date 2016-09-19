# This is not a real test suite, just a lazy convenience when developing

echo "#### building rest-query"
echo
docker build -t rgardler/acs-logging-test-rest-query:test .

echo
echo "#### running rest-query"
echo
id=$(docker run -d -p 5000:5000 --env-file ../env.conf rgardler/acs-logging-test-rest-query:test)
sleep 2
docker ps -l

echo
echo "#### check it's alive"
echo
curl http://localhost:5000/

echo
echo "#### enqueue a message"
echo
echo "GET Length"
echo
curl http://localhost:5000/queue/acsloggingdev

echo
echo "#### stop rest-query"
echo
docker stop $id

echo
echo "####  output rest-query logs"
echo
docker logs $id
