import os

UNPROCESSED_LOG_FILE='/logs/queue.log'
PROCESSED_LOG_FILE='/logs/archive.log'
SUMMARY_LOG_FILE='/logs/summary.log'

SLACK_WEBHOOK=os.getenv('SLACK_WEBHOOK')

SMTP_SERVER=os.getenv('SMTP_SERVER')
SMTP_PORT=os.getenv('SMTP_PORT')
SMTP_USERNAME=os.getenv('SMTP_USERNAME')
SMTP_PASSWORD=os.environ.get('SMTP_PASSWORD')
MAIL_FROM=os.getenv('MAIL_FROM')
MAIL_TO=os.getenv('MAIL_TO')

# Queue details
ACS_LOGGING_QUEUE_TYPE=os.getenv('ACS_LOGGING_QUEUE_TYPE', 'AzureStorageQueue')

# Azure Storage Account details
AZURE_STORAGE_ACCOUNT_NAME=os.getenv('AZURE_STORAGE_ACCOUNT_NAME')
AZURE_STORAGE_ACCOUNT_KEY=os.getenv('AZURE_STORAGE_ACCOUNT_KEY')

# Queue Details
AZURE_STORAGE_QUEUE_NAME=os.getenv('AZURE_STORAGE_QUEUE_NAME')
AZURE_STORAGE_SUMMARY_TABLE_NAME=os.getenv('AZURE_STORAGE_SUMMARY_TABLE_NAME')

# number of simulation events to create on each run (0 means conmtinue until stopped)
SIMULATION_ACTIONS=os.getenv('SIMULATION_ACTIONS', 2)
# number of seconds to delay between each logging event
SIMULATION_DELAY=os.getenv('SIMULATION_DELAY', 30)

# By default, Analyzer exits once there is nothing left on the queue
ANALYZER_KEEP_RUNNING=os.getenv('ANALYZER_KEEP_RUNNING', False)

# If we're keeping running between analyses, once we've pulled everything off sleep this long before rechecking the queue
ANALYZER_SLEEP_TIME=os.getenv('ANALYZER_SLEEP_TIME', 0)

