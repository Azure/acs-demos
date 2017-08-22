# acs-demos
Demos for Azure Container Service. These demos are designed to be
run using the [SimDem](http://github.com/rgardler/simdem) tool,
although they are just markdown files so you can read through them and
run them manually if you prefer.

# Running Demo's

All demo's are written in markdown format, so just work through them
as descripted in the `script.md` files. Alternatively you can automate
them using the [http://github.com/Azure/simdem](SimDem)
tool. SimDem allows you to run the demo's in a simulated environment
(no typing errors during your live presentations!). For more
informaiton on SimDem and what it can do simply install Docker and run
`docker run -it rgardler/simdem_cli`.

We provide a handy script to run these demo's using SimDem. Once
Docker is installed simply use the command:

`./scripts/run.sh <FLAVOR> <DEMO_DIR> [MODE]`

In this command `FLAVOR` should either be `cli` or `novnc`. The `cli`
flavor is for demo's that have no graphical content at all and can be
run in a bash shell alone. The `novnc` flavor is for demos that have a
graphical element and need to be run in a desktop
environment. `DEMO_DIR` is the name of the directory containing your demo script(s).

If using `novnc` you should point your browser at
`http://HOST:8080/?password=vncpassword` to access your demo
environment. Once there you can run a demo with the command `simdem`
into a terminal.

# Building Self Contained Demos using Docker

The script `scripts/build_container.sh` makes it easy to build self
container demos using Docker. Simply provide a name for the container
image (whih can include a repository) and a
directory containing the required demo scripts. 

```
./scripts/build_container.sh <IMAGE_NAME> <DEMO_DIR>
```

For example:

```
./scripts/build_container.sh kubernetes_demo kubernetes
```

This will build both the CLI and NoVNC containers. You can run the CLI
version with:

```
docker run -it kubernetes_demo_cli
```

To run the NoVNC version:

```
docker run -d -p 8080:8080 kubernetes_demo_novnc
```

To access the application point your browser at
`http://HOST:8080/?password=vncpassword`

## Incubator

The incubator folder is the place for incoming demos. These are
probably less complete than demos in the root. It might require some
digging around to understand how to build and use them. Once a number
of people have been able to reproduce an incubating demo then we will
move it into the root.

If you work on an incubating demo the most important thing you can do
is provide documentation and scripting that makes it as easy as
possible to reproduce. If you have problems getting an incubating demo
to work then raise an issue and we will try to help.

## Contribution

We welcome contributions in the form of documentation, bug reports,
feature requests, new features, code contributions, scripts,
screencasts and more. Please use the issue tracker and pull requests
to make your contributions.

## Code of conduct

This project has adopted the
[Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For
more information see the
[Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with
any additional questions or comments.
