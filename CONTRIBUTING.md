# Getting Started

Our goal is for your to contribute, so we make things as simple as
possible. Of course the more complete your contribution is the more
helpful it will be for others. We'll work with you to improve your
contributions if you are willing, otherwise each small step is a step
forward - so we'll thank you.

`git clone git@github.com:Azure/acs-demos.git`

`cd acs-demos/incubator/kubernetes` (or appropriate dir)

`mkdir MY_DEMO_NAME`

`cd MY_DEMO_NAME`

`emacs README.md` *you can use Vi (or even something else) if you really want :-D

## Building a Demo

This is about getting started, so we won't go into all the
requirements to graduate a demo from the incubator, lets instead focus
on the minumum expected in a pull request for a new demo.

    1. A paragraph on what the demo does
    2. The shell commands you would run to do a demo of your choice
       (enclose them in code blocks using ```)
	3. <Bonus points> example output expected from the command (write
       “Results:” on a line by itself, follow with your results in a
       code block using ```)
    4. <Bonus points> descriptive text for each command before the
       initial code block (don’t worry about it being fully
       descriptive, we can build on it)
				
What you don’t need to do to have a PR accepted into the incubator:
				
    1. Document creation / tear down of ACS cluster (SimDem will
       handle this for you, we'll teach you how)
	2. Make it work in SimDem (though huge bonus points if you do –
       let me know if you want some guidance)


Some Examples:
						
Here’s a complete example: https://github.com/Azure/acs-demos/blob/master/kubernetes/vamp/install/README.md
						
And a work in progress (minmal coverage of all 4 points above:  https://github.com/Azure/acs-demos/blob/master/incubator/kubernetes/spark/install/README.md
						
# Issue a PR
						
Issue a PR against the github.com/Azure/acs-demos repo. Please put
them in the incubator folder. Use the existing directory structure or
a new one, we don’t care we’ll move them as structure emerges.
