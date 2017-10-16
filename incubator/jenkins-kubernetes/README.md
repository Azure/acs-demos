# Installing a Jenkins Service on Azure Container Service

## Prerequesites

  * [Kubernetes Cluster](https://docs.microsoft.com/en-us/azure/container-service/kubernetes/container-service-kubernetes-walkthrough)
  * [Helm installed](https://github.com/kubernetes/helm#install)

## Install Kube Lego and Kubernetes Ingress with Nginx

[kube-lego](https://github.com/jetstack/kube-lego) automatically requests certificates for Kubernetes Ingress resources from Let's Encrypt.

Fill with your valid email address and execute the following command:

`helm install stable/kube-lego --set config.LEGO_EMAIL=<valid-email>,config.LEGO_URL=https://acme-v01.api.letsencrypt.org/directory`

Results:

```
NAME:   quiet-eagle
LAST DEPLOYED: Mon Oct 16 16:10:51 2017
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1beta1/Deployment
NAME                   DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
quiet-eagle-kube-lego  1        1        1           0          0s


NOTES:
This chart installs kube-lego to generate TLS certs for Ingresses.

EXAMPLE INGRESS YAML:

apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: example
  namespace: foo
  annotations:
    kubernetes.io/ingress.class: nginx
    # Add to generate certificates for this ingress
    kubernetes.io/tls-acme: 'true'
spec:
  rules:
    - host: www.example.com
      http:
        paths:
          - backend:
              serviceName: exampleService
              servicePort: 80
            path: /
  tls:
    # With this configuration kube-lego will generate a secret in namespace foo called `example-tls`
    # for the URL `www.example.com`
    - hosts:
        - "www.example.com"
      secretName: example-tls
```

Now install the Nginx Ingress component for Kubernetes:

`helm install stable/nginx-ingress`

Wait for the component to be installed and follow the instructions printed by Helm to retrieve the public IP address of the nginx-ingress service.

Results:

```
NAME:   giddy-bobcat
LAST DEPLOYED: Mon Oct 16 16:11:23 2017
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1beta1/Deployment
NAME                                        DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
giddy-bobcat-nginx-ingress-controller       1        1        1           0          5s
giddy-bobcat-nginx-ingress-default-backend  1        1        1           1          5s

==> v1/ConfigMap
NAME                                   DATA  AGE
giddy-bobcat-nginx-ingress-controller  1     5s

==> v1/Service
NAME                                        CLUSTER-IP    EXTERNAL-IP  PORT(S)                     AGE
giddy-bobcat-nginx-ingress-controller       10.0.202.208  <pending>    80:31295/TCP,443:32203/TCP  5s
giddy-bobcat-nginx-ingress-default-backend  10.0.134.188  <none>       80/TCP                      5s


NOTES:
The nginx-ingress controller has been installed.
It may take a few minutes for the LoadBalancer IP to be available.
You can watch the status by running 'kubectl --namespace default get services -o wide -w giddy-bobcat-nginx-ingress-controller'

An example Ingress that makes use of the controller:

  apiVersion: extensions/v1beta1
  kind: Ingress
  metadata:
    annotations:
      kubernetes.io/ingress.class: nginx
    name: example
    namespace: foo
  spec:
    rules:
      - host: www.example.com
        http:
          paths:
            - backend:
                serviceName: exampleService
                servicePort: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
        - hosts:
            - www.example.com
          secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls
```

Update your DNS provider to add a DNS record (A) that points to the external IP address of the nginx-ingress service

```
*.acs-k8s.your-domain.com in A <nginx-ingress service external IP address>
```

Wait for the DNS propagation to be completed.

## Installing Jenkins

`jenkins-values.yaml` contains configuration parameters for your
Jenkins installation. Edit this file as necessary, especially to replace the DNS entries with yours :-) 

To install Jenkins run the following command:

`helm install --namespace jenkins --name jenkins -f jenkins-values.yaml stable/jenkins`

Results:

```
NAME:   jenkins
LAST DEPLOYED: Mon Oct 16 17:19:38 2017
NAMESPACE: jenkins
STATUS: DEPLOYED

RESOURCES:
==> v1/Secret
NAME             TYPE    DATA  AGE
jenkins-jenkins  Opaque  2     7s

==> v1/ConfigMap
NAME                   DATA  AGE
jenkins-jenkins        3     7s
jenkins-jenkins-tests  1     7s

==> v1/PersistentVolumeClaim
NAME             STATUS  VOLUME                                    CAPACITY  ACCESSMODES  STORAGECLASS  AGE
jenkins-jenkins  Bound   pvc-7512c815-b285-11e7-a09f-000d3a26f11a  8Gi       RWO          default       7s

==> v1/Service
NAME                   CLUSTER-IP    EXTERNAL-IP  PORT(S)    AGE
jenkins-jenkins-agent  10.0.167.233  <none>       50000/TCP  7s
jenkins-jenkins        10.0.86.1     <none>       8080/TCP   7s

==> v1beta1/Deployment
NAME             DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
jenkins-jenkins  1        1        1           0          7s

==> v1beta1/Ingress
NAME             HOSTS                                ADDRESS  PORTS  AGE
jenkins-jenkins  jenkins.acs-k8s.juliencorioland.net  80       7s


NOTES:
1. Get your 'admin' user password by running:
  printf $(kubectl get secret --namespace jenkins jenkins-jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode);echo

2. Visit http://jenkins.acs-k8s.juliencorioland.net

3. Login with the password from step 1 and the username: admin

For more information on running Jenkins on Kubernetes, visit:
https://cloud.google.com/solutions/jenkins-on-container-engine
```

Confirm the pod has started:

`watch kubectl get pods --namespace jenkins`

Results:

```
Every 2.0s: kubectl get pods --namespace jenkins                                                                                                                                                                     Mon Oct 16 17:06:30 2017

NAME                               READY     STATUS    RESTARTS   AGE
jenkins-jenkins-4019532517-bglg9   1/1       Running   0          53s
```

## Login to Jenkins

In the output of the install there will be a script to retrieve the user password from the Jenkins instance (NOTES #1). It will look something like the below, pay special attention to the name (in this case `jenkins-jenkins`):

`printf $(kubectl get secret --namespace jenkins jenkins-jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode);echo`

Make a note of the password an log in to the Jenkins cluster by visiting the service using the hostname you have configured in the `jenkins-values.yaml` file.

Log in with the username `admin` and the password you retreived earlier. You may want to change your admin password and/or create a new user at this point.
