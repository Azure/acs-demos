import config
import notify 
from log import Log
from messageQueue import Queue
from summaryTable import SummaryTable

import json
import sys
import traceback

class MatchAnalyzer:

    def __init__(self):
        self.log = Log()
        self.msgQueue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=config.HALO_QUEUE_MATCH)
        self.summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)

    def processMatch(self, match):
        gamertag = match["Players"][0]["Player"]["Gamertag"]
        result_code = match["Players"][0]["Result"]
        if result_code == 3:
            result = 'won'
        elif result_code == 2:
            result = 'tied'
        elif result_code == 1:
            result = 'lost'
        else:
            result = 'DNF'
        count_type = gamertag + 'match' + result
        count = self.summary.getCount(count_type)
        count = count + 1
        self.summary.updateCount(count_type, count)
        print(count_type + ' is now ' + str(count))
        
    def fullAnalysis(self):
        while True:
            matches = self.msgQueue.dequeue()
            if len(matches) > 0:
                for match_event_raw in matches:
                    match_event = json.loads(match_event_raw.message_text)
                    match = json.loads(match_event["data"])
                    # print("Processing:\n" + json.dumps(match, sort_keys=True, indent=4))
                    try:
                        self.processMatch(match)
                        self.msgQueue.delete(match_event_raw)
                    except:
                        e = sys.exc_info()[0]
                        self.log.error("Could not process: " + match_event_raw.message_text + " because %s" % e)
                        traceback.print_exc(file=sys.stdout)
            else:
                break

    def getPlayerStats(self, gamertag):
        stats = {}
        stats.player = { "gamertag": gamertag }
        count_type = gamertag + "won"
        count = self.summary,getCount(count_type)
        print(str(count), wins)

if __name__ == "__main__":
    analyzer = MatchAnalyzer()  
    analyzer.fullAnalysis()
    
