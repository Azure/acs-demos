import os

UNPROCESSED_LOG_FILE='/logs/queue.log'
PROCESSED_LOG_FILE='/logs/archive.log'
SUMMARY_LOG_FILE='/logs/summary.log'

SLACK_WEBHOOK=os.getenv('SLACK_WEBHOOK')

SMTP_SERVER='smtp-mail.outlook.com'
SMTP_PORT=587
SMTP_USERNAME='ross@gardler.org'
SMTP_PASSWORD=os.environ.get('SMTP_PASSWORD')
MAIL_FROM='ross@gardler.org'
MAIL_TO='rogardle@microsoft.com'

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
