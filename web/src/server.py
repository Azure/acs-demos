"""
A simple web interface for examining the Queue and data.
"""

import config
from messageQueue import Queue
from summaryTable import SummaryTable

from flask import Flask, jsonify, redirect, request, render_template, url_for

app = Flask(__name__)

@app.route("/")
def index(name=None):
    table_service = getTableService() 
    queue_service = getQueueService()

#    queue_messages = queue_service.peek_messages(5)
#    messages = []
#    for msg in queue_messages:
#        messages.append(msg.message_text)
 
    return render_template('index.html', length = queue_service.getLength(),
                           error = str(table_service.getCount("ERROR")),
                           warning = str(table_service.getCount("WARNING")),
                           info = str(table_service.getCount("INFO")),
                           debug = str(table_service.getCount("DEBUG")),
                           correct = str(table_service.getCount("CORRECT")),
                           incorrect = str(table_service.getCount("INCORRECT")),
                           other = str(table_service.getCount("OTHER")),
#                           messages = messages
                       )

@app.route("/send", methods = ['POST'])
def sendMessage(message=None):
    message = request.form['message']

    queue_service = getQueueService()
    queue_service.enqueue(message)
    
    return redirect(url_for('index'))



def getQueueService():
  queue_service = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)
  return queue_service

def getTableService():
  return SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
