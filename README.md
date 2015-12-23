A simple logging simulator used to test long running microservices on Azure Container Service.

This project consists of a number of Docker containers each desinged to perform a specific function. The main containers are:

  * logging_base - the container on which all other containers are based
  * simulate_logging - a container that simluates a period of logging activity, writes log items into the queue
  * analyze_logs - reads log queue and creates summary log data

# Running the tests

Configure the simulate_logging container to run always. Configure the analyze_logs container to run periodically (e.g. every 20 mins).
