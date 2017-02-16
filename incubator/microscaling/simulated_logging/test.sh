# This is not a real test suite, just a lazy convenience when developing

echo "#### building producer"
echo
docker build -t rgardler/acs-logging-test-rest-produce:test .

echo
echo "#### running producer"
echo
id=$(docker run --env-file ../env.conf rgardler/acs-logging-test-rest-produce:test)

echo
echo "#### stop producer"
echo
docker stop $id

echo
echo "####  output producer logs"
echo
docker logs $id
