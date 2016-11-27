Olympics is the first game we have created, this document 
contains information about how the application has been 
built. For instructions on building and running the 
application see our [build and run](build.md) documentation.

# Core Module

The Core module (Java packages in 
`org.gardler.biglittlechallenge.core.*` are designed
to be reusable across different kinds of card games. They provide
basic building blocks for such games. However, the Core module
does not include any rules or game specific items.

# Olympics Module

The Olympics module (Java packages in 
`org.gardler.biglittlechallenge.olympics.*` extend
the Core module and add game specific rules and objects.

To play a game of Olympics simply execute the main method in
`org.gardler.biglittlechallenge.olympics.App`.
If you want to learn how to build your own game, or contribute
Olympics this is the place to start.

## How it Works

At the time of writing (and therefore not necessarily at the time 
you are reading) control is centered around a class that extends 
`org.gardler.biglittlechallenge.core.ui.AbstractUI`.
This class must provide all interaction with the players and will
manage the flow of the game. Currently we only have a Shell UI for
the game (`org.gardler.biglittlechallenge.olympics.ui.Shell`)
which will use a standard console for input and output.

If you want to implement an AI player then they will be implemented 
as an `AbstractUI`