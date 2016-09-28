"""This container doesn't really do any work, it simply consumes CPU
to simulate a batch job.

This is a simulation of a low priority batch workload that is to be
run in spare capacity in the cluster. The scale health request will
indicate that it should be scaled down if any container, other than
itself, in the cluster is in a `waiting` state. If there is no
container in a waiting state then it will indicate a desire to scale
up.

This will result in the batch workload scaling up until there is no
capacity left in the cluster.'

"""

from flask import Flask, jsonify
import json
import requests
import socket

class Batch:
    app = Flask(__name__)
    
    @app.route("/")
    def scale_need():
        """Returns a value between -100 and 100. -100 indicates no work, scale
        down immediately. +100 indicates, far too much work, scale up
        immediately. 0 means, just right, don't change anything. Values in
        betwwen indicate a scale of confidence the application has in coping
        with current load.
        """

        resp = requests.get("http://leader.mesos/marathon/v2/queue")
        queue = json.loads(resp.text)["queue"]
        isOtherWaiting = False
        isBatchWaiting = False
        for item in queue:
            if item["app"]["id"] != "/microscaling/batch":
                isOtherWaiting = True
            else:
                isBatchWaiting = True
                
        status = 0
        if isOtherWaiting:
            # A deployment is waiting for resources so this container should scale down
            status = -100
        elif not isBatchWaiting:
            # There isn't a deployment of batch in the queue so lets add another
            status = 100
        else:
            # We are already waiting for a new version of the batch job so don't request another
            status = 0
            
        hostname = socket.gethostname()

        response = {
            "container": hostname,
            "status": status
        }
        
        return jsonify(response)

if __name__ == "__main__":
    batch = Batch()

    batch.app.debug = True
    batch.app.run(host='0.0.0.0')
