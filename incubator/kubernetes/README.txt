Here you will find some ACS (Kubernetes) demos / tutorials. You can
work through them as text documents, just like any tutorial, or you
can use the SimDem tool to help guide you.

# Running in Simdem

SimDem is a tool that allows these documents to become living demos or
tutorials. The best way to understand this is simply to try
it. Install Docker and ensure your DOCKER_HOST is set correctly. Then
run:

```
./scripts/run.sh novnc incubator/kuberntes
```

and point your browser at `http://HOST:8080?password=vncpassword`

You will be presented with a Linux desktop. You can now interact with
SimDem through a terminal.

## Using Simdem

The default mode for SimDem is "tutorial". In this mode you step
through descreiptive text and related commands interactively. Execute
the command:

``` 
simdem 
```

Similar to this is "learn" mode, this presents the same descriptive
text but requires that you type the commands, rather than have them
entered for you. Execute the command:

```
simdem learn
```

If you want to deliver a demo SimDem can help here too. In "demo" mode
the descriptive text is not displayed and each time you press a key
the next command is "typed". This allows you to focus on delivering
the story while SimDem ensures your demo runs smoothly. Execute the
command"

```
simdem demo
```

HINT: if you forget why a command needs to be executed press 'd' to
display the appropriate descriptive text.

Finally, these demos / tutorials can also be run in "test' mode. Here
SimDem will work through a test plan to ensure that all demos are
working. It's a really good idea to run in this mode before delivering
a demo. An even better idea is to have these tests running in a CI system to test against all the latest code. Execute the command:

```
simdem test
```
