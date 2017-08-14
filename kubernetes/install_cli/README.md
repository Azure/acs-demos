# Install Kubernetes CLI

The Azure CLI simplifies installing the Kubernetes CLI tooling:

```
sudo az acs kubernetes install-cli
```

Results:

```
Downloading client to /usr/local/bin/kubectl from https://storage.googleapis.com/kubernetes-release/release/v1.7.3/bin/linux/amd64/kubectl
```

It can be convenient to have auto-completion:

```
source <(kubectl completion bash)
```

# Validation

Check we have the right version of the Kubernetes CLI installed.

```
kubectl version
```

Results:

```
Client Version: version.Info{Major:"1", Minor:"7", GitVersion:"v1.7.2", GitCommit:"922a86cfcd65915a9b2f69f3f193b8907d741d9c", GitTreeState:"clean", BuildDate:"2017-07-21T08:23:22Z", GoVersion:"go1.8.3", Compiler:"gc", Platform:"linux/amd64"}
```

