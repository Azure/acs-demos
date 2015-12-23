# Create a simulated log file
import config
import logging
import os
import random

logging.basicConfig(level=logging.DEBUG, format=' %(asctime)s - %(levelname)s - %(message)s')

_action_count = 5
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
  logging.debug('Simulate ' + str(_action_count) + ' actions')
  queue = open(config.UNPROCESSED_LOG_FILE, 'w')

  temp = 70;

  for i in range(_action_count):
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

  queue.close()
