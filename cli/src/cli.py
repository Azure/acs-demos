""" 
A basic CLI for examining the logs and their analysis.
"""

import config

import json

def printSummary():
  print ("Summary of log activity")
  print (json.dumps(readSummary()))

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
  dumpLogs()
  dumpUnprocessedLogs()
  printSummary()
