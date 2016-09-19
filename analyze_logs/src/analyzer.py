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

  time_since_last_event = None
  last_event_time = time.time()
  current_length = 0
  last_length = 0
  max_length = 5  
  
  def __init__(self):
    self.log = Log()
    self.log.debug("Storage account for analyzer: {0}".format(config.AZURE_STORAGE_ACCOUNT_NAME))
    self.msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)
    self.current_length = self.msgQueue.getLength()
    self.summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
    self.sleep_time = float(config.ANALYZER_SLEEP_TIME)
    self.log.debug("Sleep time between analyses: {0}".format(self.sleep_time))
    
  def incrementCount(self, event_type):
    count = self.summary.getCount(event_type)
    count = count + 1
    self.summary.updateCount(event_type, count)
    self.log.info(event_type + " count is now " + str(count))

  def processEvent(self, message):
    # Sleep to simulated a longer running process
    time.sleep(self.sleep_time)

    data = json.loads(message.message_text)
    event_type = data["type"]
    now = time.time() * 1000.0
    duration = now - data["time"]
    print("Duration of last event processing: " + str(duration))
    
    self.incrementCount(event_type)
    self.summary.updateLastProcessingTime(duration)

  def fullAnalysis(self):
    hostname = socket.gethostname()
    msg = hostname + ': Analyzing log event queue'
    notify.info(msg)
    while True:
      self.last_length = self.current_length
      self.current_length = self.msgQueue.getLength()
      if self.current_length > 0:
        events = self.msgQueue.dequeue()
        if len(events) > 0:
          now = time.time()
          for event in events:
            self.time_since_last_event = now - self.last_event_time
            self.last_event_time = now
            self.log.info("Dequeued: " + event.message_text)
            try:
              self.processEvent(event)
              self.msgQueue.delete(event)
              self.current_length = self.current_length - 1
              self.log.info("Counted and deleted: " + event.message_text)
            except:
              e = sys.exc_info()
              self.log.error("Could not process: " + event.message_text + " because %s" % e[0])
              self.log.error(traceback.format_tb(e[2]))
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

  status = 0
  if analyzer.current_length > analyzer.max_length:
    # queue is over the max_length so we need to scale up now
    status = 100
  elif analyzer.current_length == 0:
      # queue is empty, scale down
      status = -100
  elif analyzer.current_length > analyzer.last_length:
      # queue is growing but it's still under max_length, consider scaling up
      status = 50
  elif analyzer.current_length < analyzer.last_length:
      # queue is shrinking but it's still under max_length>0, consider scaling down
      status = -50
      
  hostname = socket.gethostname()

  response = {
    "container": hostname,
    "status": status,
    "timeSinceLastEvent": analyzer.time_since_last_event,
    "currentQueueLength": analyzer.current_length
  }

  return jsonify(response)

if __name__ == "__main__":
  threading.Thread(target=analyzer.fullAnalysis).start()

  app.debug = True
  app.run(host='0.0.0.0')

