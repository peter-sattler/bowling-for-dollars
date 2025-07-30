# Bowling for Dollars
[![Bowling for Dollars TV Show Logo](/images/bowling-for-dollars-tv-show-logo.jpg)

## Background

During an onsite job interview in 2023, I was asked to solve a coding challenge that I had 
[seen before](http://github.com/peter-sattler/ancient-text-translator/blob/main/README.md). So after being way
too honest with hiring manager (lets call him "David"), he went off in search of an alternative. He came back 
with this; a [Ten Pin Bowling](https://en.m.wikipedia.org/wiki/Tenpin_bowling) scoring calculator. Suddenly, I 
wished I had paid more attention to my oldest sister who took me to the local bowling alley growing up and 
encouraged me to learn how the scoring worked.

## TV Show

This project is dedicated to the [Bowling for Dollars](https://en.m.wikipedia.org/wiki/Bowling_for_Dollars) TV 
show that I used to watch with my grandfather. A decidedly low budget game show that I have fond memories of. Their 
logo is proudly displayed above.

## Overall Design

The system supports a single game for one player. It first captures the number of pins knocked down, then converts 
them into a frame once enough attempts have accumulated. After that, the frame is scored accordingly.

### Scoring Rules

1. The ultimate goal is to knock down all ten pins on your first turn.
2. During each frame, each player gets two attempts to knock down all ten pins. Turns are called “frames,” and 
each player plays ten frames in a game.
3. Knocking down all the pins on your first throw is called a strike.
4. If you miss at least one pin on the first throw and then knock down any remaining pins on your second throw, it's 
called a spare.
5. Open frames are simply frames that left at least one pin standing.
6. Scoring is based on the number of pins knocked down. Except, when you get a spare, you get 10 plus the number of 
   pins you knock down during your next throw. If you get a strike, you get 10 plus the number of pins you knock down
   with your next two throws.
7. If a player bowls a strike in the tenth (final) frame, they get two more throws within that frame. If they get a
   spare in the final frame, the player gets to throw one more ball.
8. Honor the foul line. If a player steps over the foul line or crosses it in any way, those pins will not count 
   toward that player's score

## Special Thanks

Special Thanks to [Bowling Genius!](https://bowlinggenius.com) for their excellent online ten pin calculator that
I used to double-check my own scoring algorithm.

So get out there and play a game or two and remember 
to always [Take The Skinheads Bowling](https://youtu.be/DlX1cQU8rxI?si=9gq_WCLXm3B-Vwha)!!!

Pete Sattler  
July 2025  
_peter@sattler22.net_  
