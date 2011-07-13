## Background

On July 9 in Berlin at the Scout-lounge a [code-retreat](http://www.coderetreat-berlin.de/) took place where a solution to the [Conway's game of life](http://en.wikipedia.org/wiki/Conways_Game_of_Life) was sought. This is a snapshot of the activities preceding the event and pairings during the event.

## Needed software

### Git

[Download and install git.](http://git-scm.com/download)

### Simple build tool

[Setup sbt](https://github.com/harrah/xsbt/wiki/Setup). This readme assumes that the sbt start script is named _sbt10_. Some commands that you can use are `test`, `compile`, and `run`.

## Clone this repo

To clone this repo and fetch all the branches invoke

   > git clone https://github.com/ppurang/gameoflife

   > cd gameoflife

   > git fetch

You should be on the master branch. Invoke the following to test that things are in order

   > sbt10 test

This makes sbt compile things and run all the tests. _Note:_ First attempt always takes a significant amount of time if this is the first invocation of sbt. It downloads scala libs, test libs etc.

An excellent time to have a break.

If you want to play around with sbt just invoke `sbt10` and you get a sbt console to play with.

## Branches and their significance

The following command shows you all the various branches

   > git branch -v

    BLANK    6007f36 some more sekeletons

    Patricia 6661e04 works

    SEB      4527679 ~

    Stefan   53e0210 works

    Wolf     5c32da3 works

    *master   5ac44db refactored to drop skeletons and made it a bit more testable

    miro     ae2d87d works

_Note:_ if you just see `master` and nothing else perhaps you forgot `git fetch`. The asterisk `*` indicates the present checked out branch.

To checkout a branch say BLANK

   > git checkout BLANK

and then you can rerun `sbt10` to see the effects of the changes in that branch. Now to the branches:

    BLANK  -  is a blank state supposed to be used as the starting point.

    master -  has a runnable demo of a possible implementation of game of life.

Other branches are results of pairing with different participants.

## Collaboration

As each pair has just 45 minutes I decided to just concentrate on the most important aspect of the game; the logic of when and how cells live or die.

I decided to call it the `StateTransition`: a function that given a 'State' returns a function that in turn given a sequence of neighbouring states returns an end state. In haskell like notation

    StateTransition :: State -> [State] -> State

An example run would be `Dead -> [Alive, Alive, Alive, Dead] -> Alive`. The number of neighbouring states doesn't matter. For an average square cell in the game it would be _8_.

The effort always was to use function composition to accomplish correct, concise and readable code.

_Note:_ Readability is always in a beholders eyes. What might be too terse and unreadable to some turns to be very readable to others. For the devs coming from a very imperative/procedural part of the programming world the results of these collaborations might appear very unreadable. But do give it a second chance. And compare the results to how you would have done it in say Ruby or Java or some such.

## Taking it for a run

To simulate game of life on the command line

   > git checkout master

   > sbt10

   >auser> run 10 10 20

The above will run a colony of size 10x10 through 20 generations.

