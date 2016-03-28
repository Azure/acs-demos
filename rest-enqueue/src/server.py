import config
from messageQueue import Queue

from flask import Flask, jsonify, redirect, request, render_template, url_for

app = Flask(__name__)

@app.route("/")
def hello():
    return render_template('index.html')

@app.route("/enqueue", methods = ['POST'])
def enqueue():
    queue = request.form['queue']
    msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=queue)

    msg = request.form['message']
    msgQueue.enqueue(msg)

    response = {
        "result": "success",
        "message": msg,
        "queue": queue,
        "storage_account": config.AZURE_STORAGE_ACCOUNT_NAME
    }
    
    return jsonify(response)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
