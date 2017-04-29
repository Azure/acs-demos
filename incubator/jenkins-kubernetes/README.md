# Installing a Jenkins Service on Azure Container Service

## Prerequesites

  * Kubernetes Cluster
  * [Helm installed](https://github.com/kubernetes/helm#install)

## Installing Jenkins

`jenkins-values.yaml` contains configuration parameters for your
Jenkins installation. Edit this file as necessary.

To install Jenkins run the following command:

`helm install stable/jenkins -f jenkins-config.yaml --name demo`

Wait for the public IP to be assigned:

`watch kubectl get services`

Confirm the pod has started:

`watch kubectl get pods`

## Login to Jenkins

In the output of the install there will be a script to retrieve the user password from the Jenkins instance. It will look something like the below, pay special attention to the name (in this case `demo-jenkins`):

`printf $(kubectl get secret --namespace default demo-jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode);echo`

Make a note of the password an log in to the Jenkins cluster by
visiting the service IP on port 8080 (obtainable using `kubectl get services`)

Log in with the username `admin` and the password you retreived earlier. You may want to change your admin password and/or create a new user at this point.

# TODO

Secure the service with HTTPS
