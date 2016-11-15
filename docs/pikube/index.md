Card games have to be run somewhere, why not on a Raspberry Pi
Kubernetes Cluster?

The Raspeberry Pi is a very cheap single board computer. Kubernetes is
a container orchestrator. Why do we need it to run the games? Well, we
don't, but it's a hardware and infrastrcuture project that some people
might be interested in - who doesn't want to build their own data
center? 

By using Docker containers for the management of
the application we make it easier for everyone to get
involved. Finally, if we make it work on the Raspberry Pi we can make
it work in the cloud too, if we wnat to.

Over time this will become a tutorial, for now it's a bunch of links
to useful resources relating to building a Raspberry Pi cluster
managed by Kubernetes.

# Shopping List

You will need:
  
  * At least 3 Raspberry Pi 3 Model B
  * As many 8Gb SD-cards (Class 10) as you have Pis
  * Ethernet Switch with as many ports as you have Pis
  * 60W USB power supply with as many ports as you have Pis
  * Stackable Pi Case (or build your own with Lego ;-)
  * As many 1ft USB cables as you have Pis
  * As many 1ft Ethernet cables as you have Pis

# Configuring the Pis

We will use
[HypriotOS](http://blog.hypriot.com/getting-started-with-docker-on-your-arm-device/),
which is a Raspian based OS with Docker included.

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
