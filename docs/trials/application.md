Trials is the first game we have created, this document 
contains information about how the application has been 
built. For instructions on building and running the 
application see our [build and run](build.md) documentation.

# Core Module

The Core module (Java packages in 
`org.gardler.biglittlechallenge.core.*` are designed
to be reusable across different kinds of card games. They provide
basic building blocks for such games. However, the Core module
does not include any rules or game specific items.

# Trials Modules

## trials-ai

This module contains an autonomous AI player. A game can consist of 
0 or more of these.

## trials-engine

This module contains the game engine. All `players` and `ai` 
players interact with the engine to play the game. 

## trials-model

This module contains shared code used by other trials modules. 
There is no executable code (other than tests) in this module.

## trials-player

This module contains the UI necessary to allow a human player 
to interact with the engine.

# How it Works

An engine exposes a REST API that allows a game to be played. 
AI and human players also expose a REST API allowing the 
engine to pro-actively engage with the players. The flow of a 
game is:

Players register intent to play

When engine has enough players, each player is notified game 
is ready to start. Players must respond with a time period or 
the game is aborted and engine waits for more players.

Once all players have indicated they are ready to play the 
engine informs all players of the event being played. Players 
respond with their chosen cards (within a required time or they 
forfeit the hand).

The engine calculates the result of the event and informs all 
players.

The process is repeated for all events in the tournament.