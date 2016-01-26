"""Manage the summary table for log messages. This table will hold
things like the count of each event type (info, debug, error etc)."""

import config
from log import Log

from azure.storage.table import TableService, Entity

import os

class SummaryTable:
    def __init__(self):
        self.log = Log()
        self.createAzureTable()

    def createAzureTable(self):
        """
        Create an Azure Table in which to store the summary results.
        """
        # FIXME: remove hard coded key information
        self.table_service = TableService(account_name='acstest', account_key='vCyk6qOZQWzGLQrBMMYsG+a2HQm0FuMyLEv1zqn1/8ll11kaAP37BrxVmfj9PWnFHSGmoEWSUXl4q6SCodFzYg==')
        self.table_service.create_table('summarytable')

    def writeCount(self, count_type, count):
        entry = {'PartitionKey': "count", "RowKey": count_type, 'total_count' : count}
        self.table_service.insert_entity('summarytable', entry)

    def updateCount(self, count_type, count):
        entry = {'total_count' : count}
        self.table_service.update_entity('summarytable', "count", count_type, entry)

    def getCount(self, event_type):
        """
        Get the total number of events of a given type.
        """

        count = 0
        entries = self.table_service.query_entities('summarytable', "PartitionKey eq 'count' and RowKey eq '" + event_type + "'")

        if len(entries) == 0:
            self.writeCount(event_type, 0)
        elif len(entries) > 1:
            raise Exception('We have more than one summary entry for ' + event_type) 
        else:
            count = entries[0].total_count

        return count
