"""
A simple web interface for examining the Queue and data.
"""

import config
from messageQueue import Queue
from summaryTable import SummaryTable

from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route("/")
def index():
    table_service = getTableService() 
    queue_service = getQueueService()

    summary = "Queue Length is approximately: " + queue_service.getLength() + "\n\n"
    summary = summary + "<table><tr><th>Event</th><th>Count</th></tr>"
    summary = summary + "<tr><td>Errors</td><td>" + str(table_service.getCount("ERROR")) + "</td></tr>"
    summary = summary + "<tr><td>Warnings</td><td>" + str(table_service.getCount("WARNING")) + "</td></tr>"
    summary = summary + "<tr><td>Infos</td><td>" + str(table_service.getCount("INFO")) + "</td></tr>"
    summary = summary + "<tr><td>Debugs</td><td>" + str(table_service.getCount("DEBUG")) + "</td></tr>"
    summary = summary + "<tr><td>Correct</td><td>" + str(table_service.getCount("CORRECT")) + "</td></tr>"
    summary = summary + "<tr><td>Incorrect</td><td>" + str(table_service.getCount("INCORRECT")) + "</td></tr>"
    summary = summary + "<tr><td>Others</td><td>" + str(table_service.getCount("OTHER")) + "</td></tr>"
    summary = summary + "</table>"
    return summary

def getQueueService():
  queue_service = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)
  return queue_service

def getTableService():
  return SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)


if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
