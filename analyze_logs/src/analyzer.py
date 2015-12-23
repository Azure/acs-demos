""" Count the number of errors in a log
"""

import config

import json
import os
import re

def count(log_type, log):
  """ Count the number of entries of a specific type
      within a log.
  """
  regex = re.compile(log_type + ' - ')
  mo = regex.findall(log)
  return len(mo)

def readLog():
  with open(config.UNPROCESSED_LOG_FILE, 'r') as f:
    log = f.read()
  return log

def readSummary():
  try:
    with open(config.SUMMARY_LOG_FILE, 'r') as f:
      summary = json.loads(f.read())
  except FileNotFoundError:
    summary = {'ERRORS': 0, 'WARNINGS':0, 'INFOS':0}
  return summary

def completeAnalysis():
  log = readLog()
  summary = readSummary()
  summary['ERRORS'] = summary['ERRORS'] + count("ERROR", log)
  summary['WARNINGS'] = summary['WARNINGS'] + count("WARNING", log)
  summary['INFOS'] = summary['INFOS'] + count("INFO", log)
  with open(config.SUMMARY_LOG_FILE, "w") as summary_file:
    summary_file.write(json.dumps(summary))
  with open(config.PROCESSED_LOG_FILE, 'a') as processed:
    processed.write(log)
  os.remove(config.UNPROCESSED_LOG_FILE)

if __name__ == "__main__":
    completeAnalysis()
