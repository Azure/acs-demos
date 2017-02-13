SERVICE_NAME=btk8s-3-service-name
RESOURCE_GROUP=myk8s-3-rg
az acs kubernetes get-credentials --resource-group=$RESOURCE_GROUP --name=$SERVICE_NAME
cp ~/.kube/config .

