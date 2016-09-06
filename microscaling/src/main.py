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


while True:
    for container in containers:
        resp = requests.get("http://" + container["name"] + ":5000")
        msg = "Scale need for analyzer: " + resp.text
        notify(msg)
        time.sleep(1)


