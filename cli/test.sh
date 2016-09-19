# This is not a real test suite, just a lazy convenience when developing

echo "#### building CLI"
echo
docker build -t rgardler/acs-logging-test-cli:test .

echo
echo "#### running CLI"
echo
docker run -it --env-file ../env.conf rgardler/acs-logging-test-cli:test summary
