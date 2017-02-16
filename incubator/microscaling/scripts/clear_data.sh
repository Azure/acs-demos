echo
echo "Delete and recreate the queue and table to ensure they are empty"
docker run --env-file ../env.conf rgardler/acs-logging-test-cli deleteQueue
docker run --env-file ../env.conf rgardler/acs-logging-test-cli deleteTable
docker run --env-file ../env.conf rgardler/acs-logging-test-cli createQueue
docker run --env-file ../env.conf rgardler/acs-logging-test-cli createTable
