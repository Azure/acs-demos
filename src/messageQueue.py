""" Manage the queue for the log messages """

import config
from log import Log

from azure.storage.queue import QueueService

import os

class Queue:
    def __init__(self, account_name, account_key, queue_name="logqueue"):
        """Initialiaze a queue. The type is set by the
        'ACS_LOGGING_QUEUE_TYPE' environment variable. If it is set to
        'AzureStorageQueue' then values must be provided for
        'account_name' and 'account_key' which are values associated
        with the Azure Storage account. 'queue_name' is optional and
        defaults to 'logqueue'.

        """
        self.log = Log()
        self.queue_type = config.ACS_LOGGING_QUEUE_TYPE
        self.queue_name = queue_name
        self.log.debug("Queue type: " + self.queue_type + " / " + self.queue_name)

        if self.queue_type == "AzureStorageQueue":
            self.createAzureQueues(account_name, account_key)
        elif self.queue_type == "LocalFile":
            self.file_queue = open(config.UNPROCESSED_LOG_FILE, 'w+')
        else:
            self.log.error("Unknown queue type: " + queue_type)

    def createAzureQueues(self, account_name, account_key):
        """
        Create a queue for unprocessed log messages. Entries in the queue
        will be dequeued, processed and deleted upon success.
        """

        global queue_service 
        queue_service = QueueService(account_name, account_key)
        queue_service.create_queue(self.queue_name)

    def close(self):
        """Perform any necessary clearnup on the queue
           at the end of a run.
        """
        if self.queue_type == "AzureStorageQueue":
            pass
        elif self.queue_type == "LocalFile":
            self.file_queue.close()
        else:
            self.log.error("Unknown queue type: " + queue_type)
        

    def enqueue(self, msg, level = "INFO"):
        msg = level + " - " + msg
        if self.queue_type == "LocalFile":
            file_queue.write(msg + '\n')
        elif self.queue_type == "AzureStorageQueue":
            queue_service.put_message(self.queue_name, msg)
        self.log.debug(msg)

    def dequeue(self):
        messages = []
        if self.queue_type == "LocalFile":
            with open(config.UNPROCESSED_LOG_FILE, 'r') as f:
                messages = f.readlines()[1]
        elif self.queue_type == "AzureStorageQueue":
            messages = queue_service.get_messages(self.queue_name)
        return messages

    def delete(self, message):
        queue_service.delete_message(self.queue_name, message.message_id, message.pop_receipt)
        #  with open(config.PROCESSED_LOG_FILE, 'a') as processed:
        #    processed.write(log)
        #  os.remove(config.UNPROCESSED_LOG_FILE)

    def delete_queue(self, queue_name):
        queue_service.delete_queue(queue_name)

    def getLength(self):
        """
        Get the approximate length of the queue
        """
        queue_metadata = queue_service.get_queue_metadata(self.queue_name)
        count = queue_metadata['x-ms-approximate-messages-count']
        return count
