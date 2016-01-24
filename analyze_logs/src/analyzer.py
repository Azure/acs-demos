"""Count the number of each log event in a log and clear them from
the queue.

"""

import config
import notify 
from log import Log
from messageQueue import Queue

import json
import os
import re
import sys
import traceback

global summary

def incrementCount(event_type):
  global summary
  log = Log()

  try:
    summary
  except NameError:
    summary = {'ERRORS': 0, 'WARNINGS':0, 'INFOS':0, 'OTHERS':0}

  summary[event_type] = summary[event_type] + 1
  log.info(event_type + " count is now " + str(summary[event_type]))

def processEvent(message):
    log = Log()
    msg = message.message_text
    event_type = "OTHERS"

    if msg.startswith("ERROR"):
      event_type = "ERRORS"
    elif msg.startswith("WARNING"):
      event_type = "WARNINGS"
    elif msg.startswith("INFO"):
      event_type = "INFOS"

    incrementCount(event_type)

def fullAnalysis():
  global summary
  log = Log()
  msgQueue = Queue()

  while True:
    events = msgQueue.dequeue()
    if len(events) > 0:
      for event in events:
        log.info("Dequeued: " + event.message_text)
        try:
          processEvent(event)
          msgQueue.delete(event)
          log.info("Counted and deleted: " + event.message_text)
        except:
          e = sys.exc_info()[0]
          log.error("Could not process: " + event.message_text + " because %s" % e)
          traceback.print_exc(file=sys.stdout)
    else:
      notify.info("Analyzed messages in queue.\n " + json.dumps(summary))
      break 

if __name__ == "__main__":
    fullAnalysis()
