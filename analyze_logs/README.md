An example analyzer that pulls messages from the queue, counts the
number of each type and writes the results to a summary table.

Messages should have an event type and message, with a ' - '
separater. For example:

```
EVENT_TYPE - EVENT MESSAGE
```
