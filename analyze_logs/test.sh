# This is not a real test suite, just a lazy convenience when developing

echo "#### building analyzer"
echo
docker build -t rgardler/acs-logging-test-rest-analyze:test .

echo
echo "#### running analyzer"
echo
id=$(docker run --env-file ../env.conf rgardler/acs-logging-test-rest-analyze:test)

echo
echo "#### stop analyzer"
echo
docker stop $id

echo
echo "####  output rest-query logs"
echo
docker logs $id
