'''
Helper class for working with Slack webhooks.
'''

import config

import json
import requests

def send(msg, channel="general"):
    payload = {
        "channel": "#" + channel,
        "text": msg
    }
    requests.post(config.SLACK_WEBHOOK, json.dumps(payload))

def info(msg):
    send(msg, 'info')

if __name__ == "__main__":
    send("Test message from ACS Logging Test Slack Bot")


