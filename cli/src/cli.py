""" 
A basic CLI for examining the logs and their analysis.
"""

import config
import notify
from log import Log
from messageQueue import Queue
from summaryTable import SummaryTable

import json
import optparse
import pickle

def printSummary():
  log = Log()
  table = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
  queue_service = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)
  summary = "Queue Length is approximately: " + queue_service.getLength() + "\n\n"
  summary = summary + "Processed events:\n"
  summary = summary + "Errors: " + str(table.getCount("ERROR")) + "\n"
  summary = summary + "Warnings: " + str(table.getCount("WARNING")) + "\n"
  summary = summary + "Infos: " + str(table.getCount("INFO")) + "\n"
  summary = summary + "Debugs: " + str(table.getCount("DEBUG")) + "\n"
  summary = summary + "Others: " + str(table.getCount("OTHER")) + "\n"
  print(summary)
  notify.info(summary)

def dumpUnprocessedLogs():
  print ("Unproccessed logs")
  try:
      with open(config.UNPROCESSED_LOG_FILE, 'r') as f:
          log = f.read()
      print (log)
  except:
      print("No logs waiting to be processed")

def dumpLogs():
  print ("Proccessed logs")
  try:
      with open(config.PROCESSED_LOG_FILE, 'r') as f:
          log = f.read()
      print (log)
  except:
      print("No logs have been processed")

def readSummary():
  try:
    with open(config.SUMMARY_LOG_FILE, 'r') as f:
      summary = json.loads(f.read())
  except FileNotFoundError:
    summary = {'ERRORS': 0, 'WARNINGS':0, 'INFOS':0}
  return summary

def deleteQueue():
  log = Log()
  queue_service.delete_queue(config.AZURE_STORAGE_QUEUE_NAME)
  log.info("Queue deleted: " + config.AZURE_STORAGE_QUEUE_NAME)
 
def deleteTable():
  log = Log()
  queue_service.delete_queue(config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
  log.info("Table deleted: " + config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
  
if __name__ == "__main__":
  log = Log()

  usage = "usage: %prog [options] command"
    
  p = optparse.OptionParser(usage=usage)
  options, arguments = p.parse_args()

  cmd = arguments[0]
  
  if cmd == "config":
    help(config)
    exit()

  queue_service = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.AZURE_STORAGE_QUEUE_NAME)


  if cmd == "summary":
    printSummary()
  elif cmd == "length":
    print(queue_service.getLength())
  elif cmd == "deleteQueue":
    deleteQueue()
  elif cmd == "deleteTable":
    deleteTable()
  else:
    log.error("Unkown command: " + cmd)
