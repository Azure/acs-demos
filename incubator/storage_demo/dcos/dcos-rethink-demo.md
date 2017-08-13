# RethinkDB Storage Demo on DCOS

This demo will demonstrate running RethinkDB on a DCOS cluster on ACS.  The demo is fairly basic because DCOS does not yet have the level of Persistent Volume support that Kubernetes does.  There are other solutions including network filesystems like GlusterFS or potentially using Flocker and the Azure Flocker Driver.  That said, ClusterHQ, the maintainers of Flocker, have ceased being in business.  The Mesosphere preferred solution is to use the RexRay driver framework.  No RexRay driver exists yet for Azure.

In this demo we will:

- Deploy an DCOS cluster using `acs-engine`
- Make use of Marathon Attributes to specify the agent that RethinkDB will run on
- Initialize the managed disks (format and mount)
- Deploy RethinkDB to the specific agent
- Interact with RethinkDB
- Kill the agent and wait and see RethinkDB get redeployed to the same agent
- Show that data in RethinkDB was preserved

## Deploying the DCOS cluster

For this demo, the cluster is deployed using `acs-engine` and will come with pre-attached managed disks.  `acs-engine` takes a template and generates the approriate ARM templates to deploy the desired configuration.

The template for this deployment is as follows:

```bash
{
  "apiVersion": "vlabs",
  "properties": {
    "orchestratorProfile": {
      "orchestratorType": "DCOS"
    },
    "masterProfile": {
      "count": 1,
      "dnsPrefix": "jmsbcdcos",
      "vmSize": "Standard_D2_v2"
    },
    "agentPoolProfiles": [
      {
        "name": "agent128",
        "count": 3,
        "vmSize": "Standard_D3_v2",
        "availabilityProfile": "AvailabilitySet",
        "storageProfile": "ManagedDisks",
        "diskSizesGB": [20, 20]
      },
      {
        "name": "agent1public",
        "count": 1,
        "vmSize": "Standard_D2_v2",
        "dnsPrefix": "jmsbcdcospub",
        "availabilityProfile": "AvailabilitySet",
        "storageProfile": "ManagedDisks",
        "diskSizesGB": [1],
        "ports": [
          80,
          443,
          8080
        ]
      }
    ],
    "linuxProfile": {
      "adminUsername": "sadmin",
      "ssh": {
        "publicKeys": [
          {
            "keyData": "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQCkyxU5ERFK7Z8SujvxxOLHeR6pRi9LJy7+ju5lnxa+0xMDBIvvwCM4dgY1ccCinfmRV3ha5riGhLEFbS8mrFXWC+0umF+iS/9+swzvHXqUnpXiqy8E04BXxYNVrOjNw7XR6aaPuq4M2/IM8UW6ZBxOtd5qhY/93oS+g9pqU4mI5ZoyofPXB6WNN4QTLjfMiBqe/r4adCv/3oNkMOVYHXp7p1zQ3H0taZEFD3Koigh6Ggb1JyAtp692r2ZlDRBtJU/VSBjFudBQlQcn4qohUmh8FnL4N2t6AWffb0BGoH4T/SVZq7ME+F6cfUNxAYZhlXoulb5gBuAb882p+1Sq703S1l3WZagyQ45tcmOsM17gWIyQdV8IRSkodVpbhSIl/9SBHrBejeVK2EnYA+WGlPKxrPSHsH1cImkArQxbpj9qBGiBrTFQCOu1sDZeRTkP3RRzaZOquN8iB+i9gCwC1Olg3OBeZq8jQcxRT05kCd/iAlNIbFTHmB2zD5YwAnY8hdCMMZwpMVU9hv7LmSNRLE/QWyVfl/VLNxox1uMJ1F9VB7oPc8Y4TyzflFTWgnaARnIZRtp3mJ0aYL/RTXf/59hMa7gnY4ShVOFo/F+JRSWT3+0eceGxl1I+ZUBh4l5MmMTSCJTdYFrreIIWXlNiKwgxs/N4EZn6mM/NeNabgFnkPw== jims@azhat"
          }
        ]
      }
    }
  }
}
```

Basically, each agent node is configured with two pre-attached disks of 20Gig each.  The rest is pretty straight forward.

To generate the templates:

```bash
jims@azhat:~/src/github/acsbc_storage/dcos/setup$ acs-engine -artifacts out/ dcos-template.json 
wrote out/apimodel.json
wrote out/azuredeploy.json
wrote out/azuredeploy.parameters.json
acsengine took 22.051761ms
```

And to deploy the generated ARM templates:

```bash
jims@azhat:~/src/github/acsbc_storage/dcos/setup$ cd out/
jims@azhat:~/src/github/acsbc_storage/dcos/setup/out$ az group deployment create --resource-group=jmsacsbc --name=acsdcosdep --template-file ./azuredeploy.json --parameters @./azuredeploy.parameters.json 
{
  "id": "/subscriptions/04f7ec88-8e28-41ed-8537-5e17766001f5/resourceGroups/jmsbcdcosrg/providers/Microsoft.Resources/deployments/acsdcosdep",
  "name": "acsdcosdep",
  "properties": {
    "correlationId": "c74ed8e2-0945-4bac-b51a-9d60823dbc08",
    "debugSetting": null,
    "dependencies": [
...
    ],
    "provisioningState": "Succeeded",
    "template": null,
    "templateLink": null,
    "timestamp": "2017-02-12T18:48:05.248423+00:00"
  },
  "resourceGroup": "jmsbcdcosrg"
}
```

The whole output is truncated as it is quite long.  At this point, the DCOS cluster is deployed into resource group `jmsbcdcosrg`.

## Add Marathon Attribute to DCOS agent

In order to log onto one of the agent machines and set the appropriate attributes, we first need to gain access to the agent machines.  The best way to do this is to publish the ssh key onto the master node and then access agent machines from there.

To access the master node, we first need it's IP Address:

```bash
jims@azhat:~/src/github/acsbc_storage/dcos/setup/out$ az network public-ip list --resource-group=jmsbcdcosrg | jq -r '.[] | select(.name | contains("dcos-master")) | .ipAddress'
13.64.112.147
```

The master node has an IP Address of `13.64.112.147`.  Deployments configure SSH to start listening on port 2200 for the first master, 2201 for the second, etc.  We only have one master, so we push the SSH private key to it:

```bash
jims@azhat:~/src/github/acsbc_storage/dcos/setup/out$ scp -P2200 -i ~/.ssh/id_jmsk8s_rsa /home/jims/.ssh/id_jmsk8s_rsa sadmin@13.64.112.147:.ssh/
The authenticity of host '[13.64.112.147]:2200 ([13.64.112.147]:2200)' can't be established.
ECDSA key fingerprint is SHA256:K7YNdK4kjgPCaK3OptE7UlTMS9kGD9t4yvWAw57n4LI.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '[13.64.112.147]:2200' (ECDSA) to the list of known hosts.
id_jmsk8s_rsa     
```

Next, let's log into the master and then the agent node we want to set our attribute on:

```bash
jims@azhat:~/src/github/acsbc_storage/dcos/setup/out$ ssh -p 2200 -i ~/.ssh/id_jmsk8s_rsa sadmin@13.64.112.147
Welcome to Ubuntu 16.04 LTS (GNU/Linux 4.4.0-28-generic x86_64)

 * Documentation:  https://help.ubuntu.com/

  Get cloud support with Ubuntu Advantage Cloud Guest:
    http://www.ubuntu.com/business/services/cloud

0 packages can be updated.
0 updates are security updates.


To run a command as administrator (user "root"), use "sudo <command>".
See "man sudo_root" for details.

sadmin@dcos-master-15497509-0:~$ ssh -i ~/.ssh/id_jmsk8s_rsa sadmin@10.0.0.6
ssh: /opt/mesosphere/lib/libcrypto.so.1.0.0: no version information available (required by ssh)
ssh: /opt/mesosphere/lib/libcrypto.so.1.0.0: no version information available (required by ssh)
The authenticity of host '10.0.0.6 (10.0.0.6)' can't be established.
ECDSA key fingerprint is SHA256:3n9aoTkSWABvueEKHatyIHqeWIKHJ7QDcfBAyACklKs.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '10.0.0.6' (ECDSA) to the list of known hosts.
Welcome to Ubuntu 16.04 LTS (GNU/Linux 4.4.0-28-generic x86_64)

 * Documentation:  https://help.ubuntu.com/

  Get cloud support with Ubuntu Advantage Cloud Guest:
    http://www.ubuntu.com/business/services/cloud

98 packages can be updated.
0 updates are security updates.


*** System restart required ***

The programs included with the Ubuntu system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Ubuntu comes with ABSOLUTELY NO WARRANTY, to the extent permitted by
applicable law.

To run a command as administrator (user "root"), use "sudo <command>".
See "man sudo_root" for details.

sadmin@dcos-agent128-154975090:~$ 
```

Next, we need to add the the attribute `MESOS_ATTRIBUTES=role:db` to `/var/lib/dcos/mesos-slave-common` and reboot the agent node:

```bash
sadmin@dcos-agent128-154975090:~$ ls /var/lib/dcos/mesos-slave-common
ls: cannot access '/var/lib/dcos/mesos-slave-common': No such file or directory
sadmin@dcos-agent128-154975090:/var/lib/dcos$ sudo su
sadmin@dcos-agent128-154975090:~$ sudo echo "MESOS_ATTRIBUTES=role:db" > /var/lib/dcos/mesos-slave-common
root@dcos-agent128-154975090:/var/lib/dcos# echo "MESOS_ATTRIBUTES=role:db" > /var/lib/dcos/mesos-slave-common
root@dcos-agent128-154975090:/var/lib/dcos# reboot
```

If you look at the node in the DCOS UI, you will note that the screen reflects  the attribute we just added.

![agent node](https://github.com/jmspring/acsbc_storage/raw/master/dcos/images/dcos-agent-view.png) 

## Set up the Managed Disk

When you deploy a DCOS cluster with pre-allocated managed disks, the disks are not formated or attached.  So, before using them, you need to set that up.  The first managed disk is attached at `/dev/sdc`, so we need to format the disk and make sure it is attached to the VM each time the VM starts.  This is done by using `mkfs.ext4` to format the disk then adding an entry to `/etc/fstab` and then rebooting the VM.  For purposes of this demo, we are mounting the managed disk at `/mnt/managed/0`.

The steps are as follows:

```bash
sadmin@dcos-agent128-154975090:~$ sudo mkfs.ext4 /dev/sdc
mke2fs 1.42.13 (17-May-2015)
Discarding device blocks: done                            
Creating filesystem with 5242880 4k blocks and 1310720 inodes
Filesystem UUID: 4fd339b5-ad5b-4f01-aff8-43df0316185a
Superblock backups stored on blocks: 
	32768, 98304, 163840, 229376, 294912, 819200, 884736, 1605632, 2654208, 
	4096000

Allocating group tables: done                            
Writing inode tables: done                            
Creating journal (32768 blocks): done
Writing superblocks and filesystem accounting information: done   
sadmin@dcos-agent128-154975090:~$ sudo mkdir -p /mnt/managed/0
sadmin@dcos-agent128-154975090:~$ sudo su
root@dcos-agent128-154975090:~$ echo "/dev/sdc                                /mnt/managed/0  ext4    defaults,discard        0 0" >> /etc/fstab"
root@dcos-agent128-154975090:~$ reboot
```

Upon reboot, log back in and check the attached disks:

```bash
sadmin@dcos-agent128-154975090:~$ mount | grep sdc
/dev/sdc on /mnt/managed/0 type ext4 (rw,relatime,discard,data=ordered)
```

# Deploying RethinkDB to the agent

In order to deploy RethinkDB, we need to specify the `marathon.json` file for RethinkDB and indicate the attributes that will make sure that RethinkDB is deployed to the node we configured for it.

The `marathon.json` file looks as follows:

```bash
{
  "id": "rethinkdb",
  "cmd": "rethinkdb --bind all -d /data/rethinkdb/db",
  "cpus": 1,
  "mem": 8000.0,
  "instances": 1,
  "container": {
    "type": "DOCKER",
    "docker": {
      "image": "rethinkdb:2.3.5",
      "network": "BRIDGE",
      "portMappings": [
        { "containerPort": 8080, "hostPort": 0, "protocol": "tcp" },
        { "containerPort": 28015, "hostPort": 0, "protocol": "tcp"}
      ]
    },
    "volumes": [
      {
        "containerPath": "/data/rethinkdb/db",
        "hostPath": "/mnt/managed/0/rethinkdb",
        "mode": "RW"
      }
    ]
  },
  "constraints": [["role", "CLUSTER", "db"]]
}
```

Basically, what this config is defining is to run RethnkDB on the node we defined having a `role` of `db` and using the mountpoint we configured.  Again, this mountpoint is static to the node.  One thing to note, we are letting Marathon pick the ports where the container ports are exposed on the host.  Generally, one would use service discovery to figure out these ports.  Since we are just interested in RethinkDB being redeployed to the same node and data being preserved, we will skip that can of worms.

To deploy the `marathon.json` file for RethinkDB, we use `curl` to post it to the Marathon REST API.  This demo assumes interaction with Marathon (for the REST API) will occur from the master node.  Deploying the config:

```bash
sadmin@dcos-master-15497509-0$ curl -X POST http://localhost:80/marathon/v2/apps -d @./marathon.json -H "Content-type: application/json"
{
    "acceptedResourceRoles": null,
    "args": null,
    "backoffFactor": 1.15,
    "backoffSeconds": 1,
    "cmd": "rethinkdb --bind all -d /data/rethinkdb/db",
    "constraints": [
        [
            "role",
            "CLUSTER",
            "db"
        ]
    ],
    "container": {
        "docker": {
            "forcePullImage": false,
            "image": "rethinkdb:2.3.5",
            "network": "BRIDGE",
            "parameters": [],
            "portMappings": [
                {
                    "containerPort": 8080,
                    "hostPort": 0,
                    "labels": {},
                    "protocol": "tcp",
                    "servicePort": 0
                },
                {
                    "containerPort": 28015,
                    "hostPort": 0,
                    "labels": {},
                    "protocol": "tcp",
                    "servicePort": 0
                }
            ],
            "privileged": false
        },
        "type": "DOCKER",
        "volumes": [
            {
                "containerPath": "/data/rethinkdb/db",
                "hostPath": "/mnt/managed/0/rethinkdb",
                "mode": "RW"
            }
        ]
    },
    "cpus": 1,
    "dependencies": [],
    "deployments": [
        {
            "id": "749ae5b4-8b6c-4f6d-9060-b99749d41aa4"
        }
    ],
    "disk": 0,
    "env": {},
    "executor": "",
    "fetch": [],
    "gpus": 0,
    "healthChecks": [],
    "id": "/rethinkdb",
    "instances": 1,
    "ipAddress": null,
    "labels": {},
    "maxLaunchDelaySeconds": 3600,
    "mem": 8000,
    "portDefinitions": [
        {
            "labels": {},
            "port": 0,
            "protocol": "tcp"
        }
    ],
    "ports": [
        0,
        0
    ],
    "readinessChecks": [],
    "requirePorts": false,
    "residency": null,
    "secrets": {},
    "storeUrls": [],
    "taskKillGracePeriodSeconds": null,
    "tasks": [],
    "tasksHealthy": 0,
    "tasksRunning": 0,
    "tasksStaged": 0,
    "tasksUnhealthy": 0,
    "upgradeStrategy": {
        "maximumOverCapacity": 1,
        "minimumHealthCapacity": 1
    },
    "uris": [],
    "user": null,
    "version": "2017-02-14T04:49:04.728Z"
}
```

When the service is deployed, we will need to log into the agent node and use `docker ps` to determine which ports on the agent node RethinkDB is using.  

```bash
sadmin@dcos-agent128-154975090:~$ sudo docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS                                                        NAMES
cd5dcd233cc1        rethinkdb:2.3.5     "/bin/sh -c 'rethinkd"   About a minute ago   Up About a minute   29015/tcp, 0.0.0.0:6724->8080/tcp, 0.0.0.0:6725->28015/tcp   mesos-5e7a265e-b477-47cf-9e30-e198de501f0e-S5.df79479a-43e5-4678-93ec-1caee739cf3e
```

So, port 28015 is mapped to host port 6725 and port 8080 is mapped to host port 6724.  We can verify that by attempting to connect to the RethinkDB port 28015 using `telnet` from the master node:

```bash
sadmin@dcos-master-15497509-0:~$ telnet 10.0.0.6 6725
Trying 10.0.0.6...
Connected to 10.0.0.6.
Escape character is '^]'.
GET /
ERROR: Received an unsupported protocol version. This port is for RethinkDB queries. Does your client driver version not match the server?
Connection closed by foreign host.
```

# Interacting with RethinkDB

So, using the app, let's interact with RethinkDB.  First, create a table named `weather`:

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ node ./index.js -h 10.0.0.6 -p 6725 -a create -t weather
{
  "config_changes": [
    {
      "new_val": {
        "db": "test",
        "durability": "hard",
        "id": "eba2146c-b272-4d7c-b530-82b4580a26b1",
        "indexes": [],
        "name": "weather",
        "primary_key": "id",
        "shards": [
          {
            "nonvoting_replicas": [],
            "primary_replica": "7cb77f07fafe_xh5",
            "replicas": [
              "7cb77f07fafe_xh5"
            ]
          }
        ],
        "write_acks": "majority"
      },
      "old_val": null
    }
  ],
  "tables_created": 1
}
```

Adding data to the table `weather`:

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ node ./index.js -h 10.0.0.6 -p 6725 -a insert -t weather -f ./data/sfo_status.json
{
  "deleted": 0,
  "errors": 0,
  "generated_keys": [
    "83d33c31-66ab-4329-943d-f334d56dfa16"
  ],
  "inserted": 1,
  "replaced": 0,
  "skipped": 0,
  "unchanged": 0
}
```

And finally showing the data:

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ node ./index.js -h 10.0.0.6 -p 6725 -a get -t weather
[
  {
    "IATA": "SFO",
    "ICAO": "KSFO",
    "city": "San Francisco",
    "delay": "true",
    "id": "83d33c31-66ab-4329-943d-f334d56dfa16",
    "name": "San Francisco International",
    "state": "California",
    "status": {
      "avgDelay": "1 hour and 48 minutes",
      "closureBegin": "",
      "closureEnd": "",
      "endTime": "",
      "maxDelay": "",
      "minDelay": "",
      "reason": "WEATHER / LOW CEILINGS",
      "trend": "",
      "type": "Ground Delay"
    },
    "weather": {
      "meta": {
        "credit": "NOAA's National Weather Service",
        "updated": "5:56 PM Local",
        "url": "http://weather.gov/"
      },
      "temp": "63.0 F (17.2 C)",
      "visibility": 10,
      "weather": "Mostly Cloudy",
      "wind": "South at 13.8mph"
    }
  }
]
```

It should be noted that the output of the `create` and `insert` commands were the results returned by RethinkDB after each operation.  The content shown for `get` is the content stored in `./data/sfo_status.json` that was inserted into the database.

# Reboot Agent and verify RethinkDB redeployed

If we look at the Marathon UI for details on the RethinkDB service, we can see that it has been running on the agent with IP address `10.0.0.6` for about thirteen hours.

![rethink-prior-to-reboot](https://github.com/jmspring/acsbc_storage/raw/master/dcos/images/rethink-prior-to-reboot.png) 

Now, let's reboot the agent:

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ ssh -i ~/.ssh/id_jmsk8s_rsa 10.0.0.6 "sudo reboot"
Connection to 10.0.0.6 closed by remote host.
```

The host should come back up shortly.  Looking at the Marathon UI for the RethinkDB service again, one sees that it is on the same node, but with different ports.

![rethink-after-reboot](https://github.com/jmspring/acsbc_storage/raw/master/dcos/images/rethink-after-reboot.png)

If you look closely, you can see the ports have changed, but using `docker ps`, we can get a better view:

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ ssh -i ~/.ssh/id_jmsk8s_rsa 10.0.0.6 "sudo docker ps"
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                                        NAMES
e02b70d9af8c        rethinkdb:2.3.5     "/bin/sh -c 'rethinkd"   5 minutes ago       Up 5 minutes        29015/tcp, 0.0.0.0:8537->8080/tcp, 0.0.0.0:8538->28015/tcp   mesos-5e7a265e-b477-47cf-9e30-e198de501f0e-S6.d87c98a5-7204-45d6-b7e2-afaf539f21ee
```

The RethinkDB protocol port `28015` is now mapped to `8538`.

# Was the data preserved?

Using the application, let's see if the data previously inserted was preserved.  We should note that the new port to connect to is `8538`.

```bash
sadmin@dcos-master-15497509-0:~/rethink-app$ node ./index.js -h 10.0.0.6 -p 8538 -a get -t weather
[
  {
    "IATA": "SFO",
    "ICAO": "KSFO",
    "city": "San Francisco",
    "delay": "true",
    "id": "83d33c31-66ab-4329-943d-f334d56dfa16",
    "name": "San Francisco International",
    "state": "California",
    "status": {
      "avgDelay": "1 hour and 48 minutes",
      "closureBegin": "",
      "closureEnd": "",
      "endTime": "",
      "maxDelay": "",
      "minDelay": "",
      "reason": "WEATHER / LOW CEILINGS",
      "trend": "",
      "type": "Ground Delay"
    },
    "weather": {
      "meta": {
        "credit": "NOAA's National Weather Service",
        "updated": "5:56 PM Local",
        "url": "http://weather.gov/"
      },
      "temp": "63.0 F (17.2 C)",
      "visibility": 10,
      "weather": "Mostly Cloudy",
      "wind": "South at 13.8mph"
    }
  }
]

