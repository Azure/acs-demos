# Create a simulated log file
import config
import mailhandler
import notify

import logging
import os
import random
import sys
import traceback

logging.basicConfig(level=logging.DEBUG, format=' %(asctime)s - %(levelname)s - %(message)s')

_action_count = 1000 # number of actions to log, 0 means continue until killed
_too_hot = 75
_just_right = 70
_too_cold = 68

global temp
global queue

def log(msg):
  queue.write(msg + '\n')
  logging.debug(msg)

def info(msg):
  msg = "INFO - " + msg
  log(msg)
  
def log_change(change):
  msg = "Change since last reading: " + str(change)
  info(msg)

def log_temp(temp):
  msg = "Current temperature: " + str(temp)
  info(msg)

def warn(msg):
  warning = "WARNING - " + msg
  log(warning)

def error(msg):
  error = "ERROR - " + msg
  log(error)

def simulate():
  global queue, temp
  if _action_count > 0:
    logging.debug('Attempting to simulate ' + str(_action_count) + ' actions')
  else:
    logging.debug('Simulating until stopped')

  queue = open(config.UNPROCESSED_LOG_FILE, 'w+')

  temp = 70;

  _actions = 1
  while _action_count == 0 or _action_count - _actions > 0:
    change = random.randint(-1, 1)
    log_change(change)

    temp = temp + change
    log_temp(temp)

    if temp == _just_right:
      info("That's perfect");
    elif temp < _just_right and temp > _too_cold:
      warn('Getting a little chilly')
    elif temp > _just_right and temp < _too_hot:
      warn('Getting a touch warm')
    elif temp <= _too_cold:
      error('Too cold, how did this happen?')
    elif temp >= _too_hot:
      error('Too hot, how did this happen?')
    else:
      error('Can''t tell if it''s hot or cold')

    _actions = _actions + 1

  queue.close()

  notify.info("Simulated " + str(_action_count) + " actions and added them to the queue")

if __name__ == "__main__":
    try:
      simulate()
    except:
      e = sys.exc_info()[0]
      logging.error("Unable to simulate logging", exc_info=True)
      notify.error("ACS Logging simulation failed")
      mailhandler.send("ACS Logging simulation failed", "Check logs for details")
