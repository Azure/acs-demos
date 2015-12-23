"""
Generate simulated logs, placing new items into the queue.
Process the Queue, generating summary data and
  appending entries to the log store
"""

import analyzer
import simulatedLogs

# simulate_logs
simulatedLogs.simulate()

# process queue
analyzer.complete()

