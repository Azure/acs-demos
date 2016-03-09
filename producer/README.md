Producer is the base container for event producers. It provides a rest
endpoint (implemented by 'src/server.py'). When this API is called it
will generate a message in the queue.

# Running

`docker run -d -p 5000:5000 --env-file env.conf rgardler/acs-logging-test-producer`

# REST API

Now POST to `http://localhost:5000/enqueue' with a a form data payload as
follows: 

`queue=<queue_name>&message=<message_text>`

The `message` will be added to the named queue. If the queue does not
yet exist it will be created in the storage account identified in the
following values (in env.conf).

```
AZURE_STORAGE_ACCOUNT_NAME
AZURE_STORAGE_ACCOUNT_KEY
```

# Building

`docker build -t rgardler/acs-logging-test-producer .`

# Testing

There is only very simple testing right now.

`./test.sh`