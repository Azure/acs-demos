This demo shows how to create a Kubernetes cluster and install Vamp on it.

# TL;DR

prep.sh: prepare your environment, takes 5-10 minutes to run

run.sh: run as a descriptive tutorial (must run prep.sh first)

demo.sh: run as a demo for third parties (must run prep.sh first)

test.sh: tears down any existing cluster, builds a cluster, runs tests against it

# Full details

The demo is interactive and can be run in different modes, either in
"tutorial" or "demo" mode. You might also choose to run each of the
comamnds manualy, in this "mode" you are simply reading a document and
following along.

Tutorial mode is ideal if you are using this as a learning tool. In
this mode a description of what you are about to do is shown on the
screen, hit a key to see the command, hit another key to execute the
command.

Demo mode is ideal if you are using this to teach or demonstrate how
to achive the goal. In this mode no descriptive text is shown, instead
when you press a key the next command is "typed", pressing another key
will execute the command. The idea is that you describe what is
happening as the application "types" the command for you.

Both the tutorial and demo modes can be run in auto mode too. This
means that the program does not wait for a keypress before
proceeding. This can be useful if you want to runthe complete script
unattended.

Manual mode is ideal if you would like to manually type the commands,
many people find this helps them remember. It can be useful in the
first few runs, but we still recommend using "demo" mode when doing
live demo's - it's much harder to make a mistake this way.

# Preparation

Creating a Kubernetes cluster takes 5-10 minutes, therefore we have
provided a `preparation` script that contains the script you need for
preparing the demo environment. This script can be run in both
tutorial and demo mode.

## Run the preparation steps in Tutorial Mode

```
./prep.sh
```

This will print out some descriptive text and wait for a
keypress. Upon the first keypress a command will be displayed, upon a
second keypress this command will be executed and the results
displayed, along with the next descriptive text block.

## Run the preparation steps in Demo Mode

```
./prep.sh --style simulate
```

On a keypress a command will be "typed" (using a simulated typing
algorithm), on a second keypress the command will be executed and the
output displayed.

## Run the preparation steps in Manual Mode

Everything you need is in `preparation/script.md`, here you will find
descriptive text, required commands and expected output.

# Running the Spark demo

The main demo script, just like the preparation script, can be run in
"tutorial", "demo" or "manual mode"

## Running the Spark demo as a tutorial

```
./run.sh
```

This will print out some descriptive text and wait for a
keypress. Upon the first keypress a command will be displayed, upon a
second keypress this command will be executed and the results
displayed, along with the next descriptive text block.

## Running as a demo

```
./run.sh --style simulate
```

On a keypress a command will be "typed" (using a simulated typing
algorithm), on a second keypress the command will be executed and the
output displayed.

## Running manually

Read and follow along with `script.md`.
