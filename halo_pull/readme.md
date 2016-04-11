# Halo API Events

This container pulls match events from the [Halo 5:
Guardians](https://developer.haloapi.com/) API and inserts them into
the queue for processing.

# How it Works

The container is designed to run on a periodic basis. Each time it
runs it will retrieve the latest matches from the Halo 5 API and add a
number of work items into a designated Storage Queue.

It is then intended that another service will retrieve these work
items from the queue and process them accordingly.

## Work Item Queues

Queues, by default, are called `haloWORK', where `WORK` identifies the
kind work item. In other words 'WORK' indicates which worker service
will process the queue. The 'halo' part of the name is configurable by
setting the value of `HALO_QUEUE_*` environment variables. See
`env.conf.tmpl`.

Messages in the queue will be a JSON data structure in the following
format:

```
    {
        "created": TIME_OF_MESSAGE_CREATION
	"data": JSON_DATA_TO_BE_PROCESSED
    }
```

TIME_OF_MESSAGE_CREATION is time at which the message was created, in
seconds since the epoch as a floating point number

JSON_DATA_TO_BE_PROCESSED is the data to be processed by the worker
attending to this queue.

# Building

```
docker-compose build
```

# Testing

```
./test.sh
```