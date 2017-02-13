## Implementation

This is the first game we have programmed. It's fairly rudimentary, 
but it works. See our [application documentation](application.md) 
for more information.

## Game Design

Think of a Deck Building game (Pokemon, Magic The Gathering etc.), a
stats game (Dungeons and Dragons) and the trials (varied
competitions) and you have the basic premise of The Big Little
Challenge.

### Team Building

Players will build teams from a range of
[characters](characters.md). These characters will be
entered into a series of competitions. The goal is to win as many of
the competitions as possible.

Each charactrer will have one special ability and a set of stats that
determine how well they perform in the various competitions. For
example, the 100m dash will require a high speed statistic, but it
will also be affected by reaction time. The 8000m race, however, will
require stamina and speed while the 8000m Steeple Chase will be more
about Stamina and Dexterity.

Characters will also have a special ability. This will give them a
specific advantage in certain circumstances. For example, "Fast Start"
will give them an advantage over other characters at the start of a
competition while "Never Stumble" means that the character will never
suffer from a fall while running.

### Traits

Players can customize their characters by assigning points to each of
the character [traits](characters.md#traits). Every
character has the same fixed number of points to distribute across
their traits.

### Competitions

There are many different kinds of
[competitions](competitions.md) that your characters can
enter. Initially these will be based on the trials, but we should
not restrict ourselves to the real world trials. Imagination is
important here.

In it's simplest form the game is just a comparison of the relavant
traits. So, in the example of the 100m sprint the winner might be
"reactions + speed + special ability". A more complex algorithm might
involve some random number generation, experience or user
interaction. There is really no limit to what could be implemented.

# Development Ideas

This section de3scribes random ideas about how the ganme might develop.

  1. Equipment cards - these give characters specials boosts, for
     example, running shoes might increase speed by 1 point. FOr pole
     vault challenge there may be a special pole that gives +1
     dexterity. Equipment is awarded when a players wins, for example,
     10 wins may allow player to draw one equipment card.

