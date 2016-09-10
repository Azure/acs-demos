./setup.sh

TAG=${1:-"latest"}

echo "Tagging and Pushing with ':$TAG'"

docker tag rgardler/acs-logging-test-base rgardler/acs-logging-test-base:$TAG
docker tag rgardler/acs-logging-test-simulate rgardler/acs-logging-test-simulate:$TAG
docker tag rgardler/acs-logging-test-rest-enqueue rgardler/acs-logging-test-rest-enqueue:$TAG
docker tag rgardler/acs-logging-test-web rgardler/acs-logging-test-web:$TAG
docker tag rgardler/acs-logging-test-analyze rgardler/acs-logging-test-analyze:$TAG
docker tag rgardler/acs-logging-test-microscaling rgardler/acs-logging-test-microscaling:$TAG
docker tag rgardler/acs-logging-test-master-proxy rgardler/acs-logging-test-master-proxy:$TAG
docker tag rgardler/acs-logging-test-cli rgardler/acs-logging-test-cli:$TAG

docker push rgardler/acs-logging-test-base:$TAG
docker push rgardler/acs-logging-test-simulate:$TAG
docker push rgardler/acs-logging-test-rest-enqueue:$TAG
docker push rgardler/acs-logging-test-web:$TAG
docker push rgardler/acs-logging-test-analyze:$TAG
docker push rgardler/acs-logging-test-microscaling:$TAG
docker push rgardler/acs-logging-test-master-proxy:$TAG
docker push rgardler/acs-logging-test-cli:$TAG

echo "Tagged and Pushing with ':$TAG'"
