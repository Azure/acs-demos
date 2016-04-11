import config 
from log import Log
from summaryTable import SummaryTable

import json

class PlayerService:
    def __init__(self):
        self.log = Log()
        self.summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)

    def getPlayer(self, gamertag):
        player = {
            "gamertag": gamertag,
            "stats": self.getStats(gamertag)
        }
        return player

    def getStats(self, gamertag):
        stats = []
        count_type = gamertag + "matchwon"
        count = self.summary.getCount(count_type)
        stats.append({"Wins": count})

        count_type = gamertag + "matchtied"
        count = self.summary.getCount(count_type)
        stats.append({"Ties": count})

        count_type = gamertag + "matchlost"
        count = self.summary.getCount(count_type)
        stats.append({"Lost": count})

        count_type = gamertag + "matchDNF"
        count = self.summary.getCount(count_type)
        stats.append({"DNF": count})
        
        return stats

if __name__ == "__main__":
    player_service = PlayerService()
    players = [player.strip() for player in config.HALO_GAMERTAGS.split(',')]
    for gamertag in players:
        print(json.dumps(player_service.getPlayer(gamertag)))

