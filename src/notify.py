'''
Helper class for working with Slack webhooks.
'''

import config

import json
import requests

def send(msg, channel=config.SLACK_GENERAL_CHANNEL):
    if config.SLACK_WEBHOOK:
        payload = {
            "channel": "#" + channel,
            "text": msg
        }
        requests.post(config.SLACK_WEBHOOK, json.dumps(payload))

def info(msg):
    send(msg, config.SLACK_INFO_CHANNEL)

def error(msg):
    send(msg, config.SLACK_ERROR_CHANNEL)

if __name__ == "__main__":
    send("Test message from ACS Logging Test Slack Bot")


