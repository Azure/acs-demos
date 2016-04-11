import config
from messageQueue import Queue
from summaryTable import SummaryTable

import http.client, urllib.request, urllib.parse, urllib.error, base64, json
import sys
import time

def get_players():
    """
    Returns a list of player gamertags to search on
    """
    players = [player.strip() for player in config.HALO_GAMERTAGS.split(',')]
    return players

def get_matches_for_player(gamertag):
    """
    Gets the latest matches involving a single player.
    """
    headers = {
        'Ocp-Apim-Subscription-Key': config.HALO_API_KEY,
    }

    start = get_match_count(gamertag)
    params = urllib.parse.urlencode({
        # Request parameters
        # 'modes': '{string}',
        'start': str(start)
        # 'count': '2'
    })

    matches = {}

    try:
        conn = http.client.HTTPSConnection('www.haloapi.com')
        conn.request("GET", "/stats/h5/players/" + gamertag +  "/matches?%s" % params, "{body}", headers)
        response = conn.getresponse()
        rawdata = response.read()
        matches = json.loads(rawdata.decode('utf-8'))
        # print(json.dumps(matches, sort_keys=True, indent=4, separators=(',', ': ')))
        conn.close()
    except Exception as e:
        sys.stderr.write('ERROR: %sn' % str(e))
        return 1

    return matches

def create_queue_entries_for_match(match):
    """
    Create all required queue entries for the supplied match.
    """
    # Increment 
    work_item = {
        "created": time.time(),
        "data": json.dumps(match)
    }

    queue_name = config.HALO_QUEUE_MATCH
    
    work_queue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=queue_name)
    work_queue.enqueue(json.dumps(work_item))

def increment_match_count(gamertag, amount = 1):
    """
    Get the count of matches we are aware of for a given player.
    """
    summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
    count = get_match_count(gamertag)
    count = count + amount
    count_type = gamertag + "_match"
    summary.updateCount(count_type, count)

def get_match_count(gamertag):
    summary = SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
    count_type = gamertag + "_match"
    return summary.getCount(count_type)

if __name__ == "__main__":
    for player in get_players():
        print("Collecting match data for " + player)
        matches = get_matches_for_player(player)
        for match in matches["Results"]:
            create_queue_entries_for_match(match)
        increment_match_count(player, matches["ResultCount"])
