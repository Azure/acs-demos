Card games have to be run somewhere, why not on a Raspberry Pi? The
Raspeberry Pi is a very cheap single board computer. It's ideal for
simple applications like this and makes for a very cheap development
computer, or even a mini data center.

In this section of the docs we'll try to help you get started with a
Raspberry Pi for development and hosting of the software we are
developing here.

By using Docker containers for the management of
the application we make it easier for everyone to get
involved. Finally, if we make it work on the Raspberry Pi we can make
it work in the cloud too, if we wnat to.

# A Rasperry Pi Data Center

For the more ambitious we recommend that you build a small
"datacenter" on which to run you applications. First you will need to
build your physical ["data center"](cluster.md) and then you will need
to install the orchestration software. You can use either
[Kuberenetes](pikube.md) or [Docker Swarm](piswarm.md).




