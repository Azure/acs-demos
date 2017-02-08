# Creates a swarm 

MASTER_IP=192.168.42.1

echo "Tear down old Swarm (if it exists)"
sshpass -p 'raspberry' ssh pi@piswarm-agent1.local docker swarm leave
sshpass -p 'raspberry' ssh pi@piswarm-agent2.local docker swarm leave
sshpass -p 'raspberry' ssh pi@piswarm-agent3.local docker swarm leave
docker swarm leave --force

echo
echo "Configure Leader"
docker swarm init --advertise-addr $MASTER_IP
JOIN_TOKEN=$(docker swarm join-token worker -q)

echo
echo "Configure Agent 1"
sshpass -p 'raspberry' ssh pi@piswarm-agent1.local docker swarm join --token $JOIN_TOKEN $MASTER_IP:2377

echo
echo "Configure Agent 2"
sshpass -p 'raspberry' ssh pi@piswarm-agent2.local docker swarm join --token $JOIN_TOKEN $MASTER_IP:2377

echo
echo "Configure Agent 3"
sshpass -p 'raspberry' ssh pi@piswarm-agent3.local docker swarm join --token $JOIN_TOKEN $MASTER_IP:2377

echo
echo "Status of Swarm Nodes"
docker node ls
