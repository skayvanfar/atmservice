echo "Pushing service docker images to docker hub ...."
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker push skayvanfar/atm-atm-service:$BUILD_NAME
docker push skayvanfar/atm-atm-nbank-service:$BUILD_NAME
