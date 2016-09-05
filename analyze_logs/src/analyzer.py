"""Count the number of each log event in a log and clear them from
the queue.

"""

import config
import notify 
from log import Log
from messageQueue import Queue
from summaryTable import SummaryTable

from flask import Flask, jsonify
import json
import os
import re
import socket
import sys
import threading
import time
import traceback

global summary

class Analyzer:

  time_since_last_event = 10
  last_event_time = time.time()
  
  def __init__(self):
    self.log = Log()
    self.log.debug("Storage account for analyzer: {0}".format(config.AZURE_STORAGE_ACCOUNT_NAME))
    self.msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)
    self.summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
    self.sleep_time = float(config.ANALYZER_SLEEP_TIME)
    self.log.debug("Sleep time between analyses: {0}".format(self.sleep_time))

  def incrementCount(self, event_type):
    count = self.summary.getCount(event_type)
    count = count + 1
    self.summary.updateCount(event_type, count)
    self.log.info(event_type + " count is now " + str(count))

  def processEvent(self, message):
    msg = message.message_text

    split = msg.find(" - ")
    if (not split):
      event_type = "OTHER"
    else:
      event_type = msg[:split]    
      
    # Sleep to simulated a longer running process
    time.sleep(self.sleep_time)

    self.incrementCount(event_type)

  def fullAnalysis(self):
    hostname = socket.gethostname()
    msg = hostname + ': Analyzing log event queue'
    notify.info(msg)
    while True:
      events = self.msgQueue.dequeue()
      if len(events) > 0:
        now = time.time()
        self.time_since_last_event = now - self.last_event_time
        self.last_event_time = now
        for event in events:
          self.log.info("Dequeued: " + event.message_text)
          try:
            self.processEvent(event)
            self.msgQueue.delete(event)
            self.log.info("Counted and deleted: " + event.message_text)
          except:
            e = sys.exc_info()[0]
            self.log.error("Could not process: " + event.message_text + " because %s" % e)
            traceback.print_exc(file=sys.stdout)
      time.sleep(self.sleep_time)   


app = Flask(__name__)
analyzer = Analyzer()
  
@app.route("/")
def scale_need():
  """Returns a value between -100 and 100. -100 indicates no work, scale
  down immediately. +100 indicates, far too much work, scale up
  immediately. 0 means, just right, don't change anything. Values in
  betwwen indicate a scale of confidence the application has in coping
  with current load.
  """

  if (analyzer.time_since_last_event < 2):
    status = 100
  elif (analyzer.time_since_last_event > 5):
    status = -100
  else:
    status = 0

  response = { "status": status }

  return jsonify(response)

if __name__ == "__main__":
  app.debug = True
  app.run(host='0.0.0.0')

  threading.Thread(target=analyzer.fullAnalysis).start()
