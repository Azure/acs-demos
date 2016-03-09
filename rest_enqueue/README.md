rest-enqueue provides a REST interface that enables the posting of
messages to a queue (implemented by 'src/server.py').

# Running

`docker run -d -p 5000:5000 --env-file env.conf rgardler/acs-logging-test-rest-enqueue`

# REST API

## Enqueue

POST to `http://localhost:5000/enqueue' with a a form data payload as
follows: 

`queue=<queue_name>&message=<message_text>`

The `message` will be added to the named queue. If the queue does not
yet exist it will be created in the storage account identified in the
following values (in ../env.conf).

```
AZURE_STORAGE_QUEUE_NAME
AZURE_STORAGE_ACCOUNT_NAME
AZURE_STORAGE_ACCOUNT_KEY
```

### Response

The response will be a JSON string that looks something like:

```
{
    result: "success",
    message: "The message posted to queue",
    queue: "The queue posted to"
    storage_account: "The storage account in which the queue exists"
}
```

# Building

`docker build -t rgardler/acs-logging-test-rest-enqueue .`

# Testing

There is only very simple testing right now.

`./test.sh`