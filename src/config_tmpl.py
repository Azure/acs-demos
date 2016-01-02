import os

UNPROCESSED_LOG_FILE='/logs/queue.log'
PROCESSED_LOG_FILE='/logs/archive.log'
SUMMARY_LOG_FILE='/logs/summary.log'

SLACK_WEBHOOK='https://hooks.slack.com/services/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'

SMTP_SERVER=
SMTP_PORT=
SMTP_USERNAME=
SMTP_PASSWORD=os.environ.get('SMTP_PASSWORD')
MAIL_FROM=
MAIL_TO=


# number of simulation events to create on each run (0 means conmtinue until stopped)
SIMULATION_ACTIONS=1000
# number of seconds to delay between each logging event
SIMULATION_DELAY=30
