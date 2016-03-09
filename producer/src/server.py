import config
from messageQueue import Queue

from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def hello():
    return "This is the REST endpoint for the producer. PUT to `/enqueue`\n"

@app.route("/enqueue", methods = ['PUT'])
def enqueue():
    queue = request.form['queue']
    msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=queue)

    msg = request.form['message']
    msgQueue.enqueue(msg, "INFO")

    return "FIXME: Create a proper response. We put a message: " + msg + " in queue " + queue + "\n"

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
