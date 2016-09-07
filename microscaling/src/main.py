"""
This is where the work is done. 
"""

import notify

import json
import requests
import time

containers = [
    {
        "name": "analyzer-microscaling.marathon.slave.mesos"
    }
]

last_scale_up_time = time.time()
last_scale_down_time = time.time()
scale_factor = 5

def scaleUp():
    global last_scale_up_time
    last_scale_up_time = time.time()
    notify.info("FIXME: need to scale up")

def scaleDown():
    global last_scale_down_time
    last_scale_down_time = time.time()
    notify.info("FIXME: need to scale down")

while True:
    for container in containers:
        resp = requests.get("http://" + container["name"] + ":5000")
        scale_health = json.loads(resp.text)
        
        status = scale_health["status"]
        now = time.time()
        if status < 0:
            time_since_last_scale = now - last_scale_down_time
            status = status - (time_since_last_scale * scale_factor)
        elif status > 0:
            time_since_last_scale = now - last_scale_up_time
            status = status + (time_since_last_scale * scale_factor)

        msg = "Scale need for analyzer: " + str(status) + "\n (time since lsat scale = " + str(time_since_last_scale) + ", scale factor = " + str(scale_factor) + ", scale health from container = " + str(scale_health["status"]) + ")"
        notify.info(msg)
            
        if  status >= 100:
            scaleUp()
        elif status <= -100:
            scaleDown()

        time.sleep(5)


        


