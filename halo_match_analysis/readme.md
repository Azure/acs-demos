This is a worker service that consumes the queue defined in
`HALO_QUEUE_MATCH`. It process the data in that queue and produces
statistical outputs.

# Analysis Performed

  * Counts the number of matches won, lost, tied and DNF for each player
    * Stored in GAMERTAGwon, GAMERTAGlost, GAMERTAGtied and GAMERTAGdnf