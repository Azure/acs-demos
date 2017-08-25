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
kubectl version --client
```

Results:

```
Client Version: version.Info{Major:"1", Minor:"7", GitVersion:"v1.7.4", GitCommit:"793658f2d7ca7f064d2bdf606519f9fe1229c381", GitTreeState:"clean", BuildDate:"2017-08-17T08:48:23Z", GoVersion:"go1.8.3", Compiler:"gc", Platform:"linux/amd64"}
```

