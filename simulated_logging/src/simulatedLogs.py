# Create a simulated log file
import config
import mailhandler
import notify

import logging
import os
import random
import socket
import sys
import time
import traceback

logging.basicConfig(level=logging.DEBUG, format=' %(asctime)s - %(levelname)s - %(message)s')

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
  hostname = socket.gethostname()
  if int(config.SIMULATION_ACTIONS) > 0:
    logging.debug(hostname + ': Attempting to simulate ' + str(config.SIMULATION_ACTIONS) + ' actions')
  else:
    logging.debug(hostname + ': Simulating until stopped')

  queue = open(config.UNPROCESSED_LOG_FILE, 'w+')

  temp = 70;

  _actions = 0
  while int(config.SIMULATION_ACTIONS) == 0 or int(config.SIMULATION_ACTIONS) - _actions > 0:
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
    time.sleep(int(config.SIMULATION_DELAY))

  queue.close()

  notify.info(hostname + ": Simulated " + str(config.SIMULATION_ACTIONS) + " actions and added them to the queue")

if __name__ == "__main__":
    try:
      simulate()
    except:
      e = sys.exc_info()[0]
      hostname = socket.gethostname()
      logging.error("Unable to simulate logging", exc_info=True)
      notify.error(hostname + ": ACS Logging simulation failed")
      mailhandler.send(hostname + ": ACS Logging simulation failed", "Check logs on " + hostname + " for details")
