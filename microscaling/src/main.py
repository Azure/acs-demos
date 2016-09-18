"""
This is where the work is done. 
"""

import config
import notify
from log import Log
from messageQueue import Queue

import json
import requests
import time
import sys


class Microscaler:
    containers = [
        {
            "id": "/microscaling/analyzer",
            "name": "analyzer-microscaling.marathon.slave.mesos"
        }
    ]

    cool_off_period = 10 # time to wait between scale requests
    last_scale_up_time = time.time()
    last_scale_down_time = time.time()
    scale_factor = 3

    def __init__(self):
        self.log = Log()
        self.msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)

    def scaleUp(self, container, status):
        """Scale health indicates that we need to scale up, calculate the new
        number of instances and issue the scale request.

        status is a number from 1 to 100, the higher the number the
        more overworked the containers are..

        """

        self.last_scale_up_time = time.time()
    
        # Get current instance count
        url = "http://leader.mesos:8080/v2/apps" + container["id"]
        resp = requests.get(url)
        app_data = json.loads(resp.text)
        instances = app_data["app"]["instances"]
    
        # Increment count
        length = self.msgQueue.getLength()
        instances = instances + int(length / 10)

        self.scale(container["id"], instances)

    def scaleDown(self, container, status):
        """Scale health indicates that we need to scale down so reduce the
        number of instances, with a minumum instance count of 1.

        status is a number from -1 to -100, the lower the number the
        more underworked the containers are.
        """

        self.last_scale_down_time = time.time()
    
        # Get current instance count
        url = "http://leader.mesos:8080/v2/apps" + container["id"]
        resp = requests.get(url)
        app_data = json.loads(resp.text)
        instances = app_data["app"]["instances"]
        if (status <= -50):
            instances = instances - 3
        else:
            instances = instances - 1
            
        self.scale(container["id"], instances, True)

    def scale(self, container_id, instances, force = False ):
        """ Scale a container to a given number of instances"""
        if (instances < 2):
            instances = 2
                   
        url = "http://leader.mesos:8080/v2/apps" + container_id
        if force:
            url = url + "?force=true"
                   
        data_packet = { "instances": instances }
        resp = requests.put(url, data = json.dumps(data_packet))

        if resp.status_code == 200:
            self.log.debug("Scaling to " + str(instances))
        else:
            self.log.debug("Problem scaling the container. Status code = " + str(resp.status_code))
                   
    def autoscale(self):
        while True:
            for container in self.containers:
                try:
                    resp = requests.get("http://" + container["name"] + ":5000")
                    scale_health = json.loads(resp.text)
        
                    status = scale_health["status"]
                    now = time.time()
                    if self.last_scale_down_time < self.last_scale_up_time:
                        time_since_last_scale = now - self.last_scale_down_time
                    else:
                        time_since_last_scale = now - self.last_scale_up_time

                    if status < 0 and time_since_last_scale > self.cool_off_period:
                        self.scaleDown(container, status)
                    elif status > 50:
                        self.scaleUp(container, status)
                    elif status > 0 and time_since_last_scale > self.cool_off_period:
                        self.scaleUp(container, status)
                except:
                    e = sys.exc_info()[0]
                    self.log.warning("Threw an error trying to get the status of the analyzer workload: " + str(e))
            time.sleep(1)

if __name__ == "__main__":
    scaler = Microscaler()
    scaler.autoscale()
        


