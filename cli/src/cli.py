""" 
A basic CLI for examining the logs and their analysis.
"""

import config
import notify
from log import Log
from summaryTable import SummaryTable

import json

def printSummary():
  log = Log()
  table = SummaryTable()
  summary = "Errors: " + str(table.getCount("ERROR")) + "\n"
  summary = summary + "Warnings: " + str(table.getCount("WARNING")) + "\n"
  summary = summary + "Infos: " + str(table.getCount("INFO")) + "\n"
  summary = summary + "Debugs: " + str(table.getCount("DEBUG")) + "\n"
  summary = summary + "Others: " + str(table.getCount("OTHER")) + "\n"
  log.info(summary)
  notify.info("Analyzed messages in queue. New counts:\n" + summary)

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

if __name__ == "__main__":
  # dumpLogs()
  # dumpUnprocessedLogs()
  printSummary()
