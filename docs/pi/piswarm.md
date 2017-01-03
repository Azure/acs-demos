Once you have built your [cluster](cluster.md) it's time to install
the orchestration software. You can use Kubernetes (this document) or
Docker Swarm as described below..

# Installing Raspian and Docker on the Raspberry Pis

  * If you bought blank SD cards:
    * Download [Raspian Lite](https://www.raspberrypi.org/downloads/raspbian/)
    * [Install](https://www.raspberrypi.org/documentation/installation/installing-images/README.md) the operating system onto your SD Card
  * Plug in an HDMI and Ethernet cable and boot the Pi
  * Connect to the Pi via SSH
    * One of the last messages output on the boot log will be the Pis IP address
	* `ssh pi@PI_IP_ADDRESS` (password defaults to 'raspberry')
  * Change the password
    * `passwd`
  * Change the hostname from `raspberrypi` to `piswarm1` (incrementing the number for each successive Pi)
    * `sudo nano /etc/hosts`
	* `sudo nano /etc/hostname`
  * It's a good idea to label the board with the hostname of the Pi
  * Reduce the GPU memory to 16Mb since these machines with be headless
    * `sudo nano /boot/config.txt` and add `gpu_mem=16` at the end
  * Install Docker
    * `curl -sSL get.docker.com | sh`
  * Set Docker to auto start
    * `sudo systemctl enable docker`
  * It can be useful to enable the Docker client for debugging purposes
    * `sudo usermod -aG docker pi`
  * Reboot the Pi
    * `sudo reboot`
  * Reconnect via SSH
    * `ssh pi@PI_IP_ADDRESS`
  * Test the installation of Docker
    * `docker run -ti hypriot/armhf-busybox`
    * After pulling the image you shuold be in a busybox container shell
	* `hostname` should return a value other than 'piswarm1'
	
# Setting up your Swarm cluster

Blatently plagiarized from the excellent
[deep dive by Alex Ellis](http://blog.alexellis.io/live-deep-dive-pi-swarm/). See
his video for a more detaild walk through.

  * Power up each of the Pis
  * SSH into piswarm1
    * `ssh pi@PI_IP_ADDRESS`
  * Verify you are not in a swarm already
    * `docker info`
	* In the output verify it says 'swarm: Inactive'
  * Initialize a Swarm
    * `docker swarm init`
	* Copy the `docker swarm join` command that is output
  * Have each of the other Pis join the Swarm by executing the above join command on each of the Pis
  * Back on 'piswarm1' verify your nodes are avialble:
    * `docker node ls`
  * Optionally make another node a manager
    * `docker node promote piswarm2`

# Deploy your first multi-container application

Blatently plagiarized from the excellent
[deep dive by Alex Ellis](http://blog.alexellis.io/live-deep-dive-pi-swarm/). See
his video for a more detaild walk through. The code for this test
application (and more) can be found on
[GitHub](https://github.com/alexellis/swarmmode-tests/tree/master/arm)

  * Create an overlay network
    * `docker network create --driver overlay --subnet 20.0.14.0/24 demonet`
  * Run a Redis instance
    * `docker service create --replicas=1 --network=demonet --name redis alexellis2/redis-arm:v6`
  * Run the node.js web front end
    * `docker service create --name counter --replicas=2 --network=demonet --publish 3000:3000 alexellis2/arm_redis_counter`
  * Check the services are healthy
    * `docker service ls`
	* You should see 1 redis and 2 counter services, if they are not
      yet active you can check the current state with `docker service
      ps counter`. A status of 'Preparing' is OK, it is still starting
      up, just be patient.
  * Call the service
    * `curl IP_ADDRESS:3000/incr`
	* Each time you call the service you will increment the count in
      redis and the current count will be returned in JSON format
  * Clean up
    * `docker network rm demonet`
    * `docker service rm redis`
    * `docker service rm counter`

# Resources

  * [Docker comes to Raspberry Pi](https://www.raspberrypi.org/blog/docker-comes-to-raspberry-pi/)
  * [Get Docker up and Running on the Raspberry Pi](http://blog.alexellis.io/getting-started-with-docker-on-raspberry-pi/)
  * [Live Deep Dive into Docker Swarm on the Pi](http://blog.alexellis.io/live-deep-dive-pi-swarm/)
