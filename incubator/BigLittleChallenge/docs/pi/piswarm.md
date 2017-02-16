Once you have built your [cluster](cluster.md) it's time to install
the orchestration software. You can use Kubernetes (this document) or
Docker Swarm as described below..

# Setting up your Swarm cluster

Blatently plagiarized from the excellent
[deep dive by Alex Ellis](http://blog.alexellis.io/live-deep-dive-pi-swarm/). See
his video for a more detaild walk through.

  * Power up each of the Pis
  * SSH into piswarm-master
    * `ssh pi@piswarm-master.local`
  * Verify you are not in a swarm already
    * `docker info`
	* In the output verify it says 'swarm: Inactive'
  * Initialize a Swarm
    * `docker swarm init`
    * Copy the `docker swarm join` command that is output

Connect to each of yor agent Pis in turn (`ssh
pi@piswarm-agent1.local`) and execute the the join command copied in
the above steps.

  * Back on 'piswarm-master' verify your nodes are avialble:
    * `docker node ls`
  * Optionally make one or more nodes a manager
    * `docker node promote piswarm-agent1.local`

# Deploy your first multi-container application

Lets check everything is working corretly:

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
