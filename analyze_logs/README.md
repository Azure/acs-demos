An example analyzer that pulls messages from the queue, counts the
number of each type and writes the results to a summary table.

Messages should have an event type and message, with a ' - '
separater. For example:

```
EVENT_TYPE - EVENT MESSAGE
```

## Environment variables
ANALYZER_SLEEP_TIME - Sleep time in seconds before checking for more items on the queue (default 0)
ANALYZER_KEEP_RUNNING - If False, exit when there are no more items on the queue to analyze (default False) 
