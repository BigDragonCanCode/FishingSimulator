# The Fisher Simulator

## Game description
In this game, I want players to experience the life of fishers. They can catch fish using different tools. They can buy
and sell stuff through the market. They need to follow the regulations when fishing, such as the size of the fish, or different kinds of license. When policies are violated, players may be fined or receive 
other punishments. More features maybe added in the future.
## Why?
I used to play a lot of fishing games while I was young. When I first came to Canada I found there are policies against 
fishing to protect the marine life. As far as I know the existing games are focusing too much on the process of catching
fish, and lack of other aspects that fishing involves. Therefore, I want to design a game that includes such features, with 
more features that make players feel like they are the real fishers.

## For whom?
- Gamers who love playing simulator games
- People who love fishing

## User Story
- As a user, I want to catch fish using fishing rods.
- As a user, I want to catch fish using fishing nets.
- As a user, I want to buy license and tools from the market.
- As a user, I want to sell captured fish in the market.
- As a user, I want to know the size of the fish.
- As a user, I want to know the unit price of the fish.
- As a user, I want to decide either to keep or to free the captured fish.
- As a user, I want to pay the fine when I violate the regulation.
- As a user, I want to receive notification when the daily capture limit is reached.
- As a user, I want to fast-forward to the second day to reset the daily capture amount.
- As a user, I want to choose to load the game or start a new game.
- As a user, I want to choose whether to save the progress or not while I quit.
- As a user, I want to save the player status.
- As a user, I want to save the fish I captured.
- As a user, I want to save the violation status.

## Instructions for Grader

- You can generate the first required action related to adding Xs to a Y by adding the fish to the storage. (click "add fish")
- You can generate the second required action related to adding Xs to a Y by removing the fish from the storage.
  - click "release fish" to simply remove it
  - OR click "sell fish" to remove it and add the sold amount to the balance.
- You can locate my visual component by
  - finding the images in the "marketplace" panel
  - OR saving a fish to the storage. (click "add fish" and click "yes" to save a fish to the inventory)
- You can save the state of my application by clicking "quit game", and choose to save or not.
  - If no game was running, the game will quit without saving anything.
- You can reload the state of my application by clicking "start game", and choose to load or start a new game.
- The license can be purchased only when **the player doesn't have one & the fine is paid up**.
- The fine can be paid here, click the button will **clear the fine to 0**. 
- must purchase  **license** and **fishing rod** before you can really add fish.
- click "start a new day" to reset the daily captured amount to 0, **if you don't want to violate the daily quota (10 fish/day) and be fined, please use this button frequently.**

## Phase 4: Task 2
- Tue Aug 08 21:22:13 PDT 2023
- Generated fisher: s
- Tue Aug 08 21:22:13 PDT 2023
- Generated an inventory with no items.
- Tue Aug 08 21:22:13 PDT 2023
- Generated a Regulation item.
- Tue Aug 08 21:22:13 PDT 2023
- Set up a market.
- Tue Aug 08 21:22:19 PDT 2023
- spent money: $120.0
- Tue Aug 08 21:22:19 PDT 2023
- bought a license.
- Tue Aug 08 21:22:21 PDT 2023
- spent money: $200.0
- Tue Aug 08 21:22:21 PDT 2023
- bought a fishing rod.
- Tue Aug 08 21:22:22 PDT 2023
- Generated fish: Steelhead, size: 63.4
- Tue Aug 08 21:22:22 PDT 2023
- Fish added to the inventory.
- Tue Aug 08 21:22:23 PDT 2023
- daily captured amount added by 1.
- Tue Aug 08 21:22:25 PDT 2023
- Generated fish: Steelhead, size: 18.42
- Tue Aug 08 21:22:25 PDT 2023
- Fish added to the inventory.
- Tue Aug 08 21:22:25 PDT 2023
- daily captured amount added by 1.
- Tue Aug 08 21:22:27 PDT 2023
- Fish removed from the inventory.
- Tue Aug 08 21:22:29 PDT 2023
- fish sold.
- Tue Aug 08 21:22:29 PDT 2023
- Added money: $73.5
- Tue Aug 08 21:22:29 PDT 2023
- Fish removed from the inventory.
- Tue Aug 08 21:22:31 PDT 2023
- reset the daily captured amount to 0.

## Phase 4: Task 3

After I draw the UML diagram for my UI package, I found out the main UI, which is the GameUI, was connected to every 
UIs in the package. There were too many path to access the gameUI which I felt was quite unecessary. Also, I found out 
that I've created some useless fields that I never used. 
I first delete all the useless fields, and then I focus on 
refactoring the associations to make them to be as less as possible. I made the UIs to only connect to the UI that 
initialized them, so now each single class is connected to 2 other classes at maximum, now the connection looks like 
the carriages of a train. By the way I really find the UML diagram helpful for designing huge
projects, they help to keep everything neat.
