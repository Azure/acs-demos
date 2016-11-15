Once you have built your [cluster](cluster.md) it's time to install
the orchestration software. You can use Kubernetes (this document) or
[Docker Swarm](piswarm.md).

# Installing Kubernetes

[kubadm](http://kubernetes.io/docs/getting-started-guides/kubeadm/)
claims to make it easy to install a secure Kubernetes cluster on
Ubuntu and HypriotOS.

## Configuring Kubernetes

### Demo Optimizations

If you are going to use the cluster in demo's it is a good idea to
[reduce the failure detection periods](https://medium.com/google-cloud/everything-you-need-to-know-about-the-kubernetes-raspberry-pi-cluster-2a2413bfa0fa#.3l4ot66dt)
in the controller manager.

# Building Containers

A good place to start for your base images for containers is the
[Hypriot provided image](https://hub.docker.com/u/hypriot/) on Docker
Hub.

# Resources

  * [Installing Kubernetes on Linux with kubeadm](http://kubernetes.io/docs/getting-started-guides/kubeadm/)
  * [Creating a Raspberry Pi cluster running Kubernetes, the shopping list (Part 1)](http://blog.kubernetes.io/2015/11/creating-a-Raspberry-Pi-cluster-running-Kubernetes-the-shopping-list-Part-1.html)
  * [Creating a Raspberry Pi cluster running Kubernetes, the installation (Part 2) ](http://blog.kubernetes.io/2015/12/creating-raspberry-pi-cluster-running.html)
  * [Everything you need to know about the Kubernetes Raspberry Pi cluster](https://opensource.com/life/16/2/build-a-kubernetes-cloud-with-raspberry-pi)
  * [Cluster computing on the Raspberry Pi with Kubernetes](https://opensource.com/life/16/2/build-a-kubernetes-cloud-with-raspberry-pi)
  * [Running Kubernetes/Docker on Raspberry Pi ](https://dzone.com/articles/running-kubernetesdocker-on-raspberry-pi)
  [Raspberry Pi Kubernetes Cluster](http://www.jinkit.com/k8s-on-rpi/)

## Cases

  * [Lego](https://github.com/Project31/kubernetes-installer-rpi/wiki)
  * [3D Printing 1](http://www.thingiverse.com/thing:1307094)
  * [3D Printing 2](https://fuzzychef.smugmug.com/Computers/ContanersContainersContainers/i-p4c7Gjk/A)
