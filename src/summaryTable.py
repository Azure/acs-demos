"""Manage the summary table for log messages. This table will hold
things like the count of each event type (info, debug, error etc)."""

import config
from log import Log

from azure.storage.table import TableService, Entity

import os

class SummaryTable:
    def __init__(self, account_name, account_key, table_name="summary"):
        """Initialiaze a table to store summary data. Values must be provided
        for 'account_name' and 'account_key' which are values
        associated with the Azure Storage account. 'table_name' is
        optional and is the name of the table used (and created if
        necessary) in the storage account.

        """
        self.log = Log()

        self.account_name = account_name
        self.account_key = account_key
        self.table_name = table_name

        self.createAzureTable()

    def createAzureTable(self):
        """
        Create an Azure Table in which to store the summary results.
        """
        self.table_service = TableService(self.account_name, self.account_key)
        self.table_service.create_table(self.table_name)

    def writeCount(self, count_type, count):
        entry = {'PartitionKey': "count", "RowKey": count_type, 'total_count' : count}
        self.table_service.insert_entity(self.table_name, entry)

    def updateCount(self, count_type, count):
        entry = {'total_count' : count}
        self.table_service.update_entity(self.table_name, "count", count_type, entry)

    def getCount(self, event_type):
        """
        Get the total number of events of a given type.
        """

        count = 0
        entries = self.table_service.query_entities(self.table_name, "PartitionKey eq 'count' and RowKey eq '" + event_type + "'")

        if len(entries) == 0:
            self.writeCount(event_type, 0)
        elif len(entries) > 1:
            raise Exception('We have more than one summary entry for ' + event_type) 
        else:
            count = entries[0].total_count

        return count
