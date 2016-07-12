# MIX Simulator and Assembler
## About
This is a project I began because I was reading The Art of Computer Programming (I recommend this book for anyone who's interested in both mathematics and computer science) and it used MIXAL, an assembly language for MIX which is a hypothetical computer. While reading this book, I finally reached the section of MIXAL (it had a relatively large section on math preceding that), I had no access to the internet because it was down (and was down for the next four days). I was new to assembly at the time, so I figured that writing this could help me better understand the book. I thought that it couldn't take more than a couple of days, but I was wrong. Yet I continued the project and this is the result. I hope this proves to be useful. 

## Status
Currently, the assembler works, but I have not made the simulator yet.

## Usage
In the terminal or command prompt, type `java assemble FILENAME`.
Once I make the simulator, you should be able to run `java MIX FILENAME` to automatically assemble the program and load the simulator. 

## To do
* ~~Finish all classes that implements AtomicExpression~~
* ~~Implement Future variables (which is going to be pretty hard)~~
* ~~There should be a part where Expression constructor attempts to evaluate the string and convert to one of Number, DefinedSymbol, or Asterick. Find this spot and actually make this work.~~
* ~~(MOST IMPORTANT) Make this work...~~
* Make a simulator.
* Learn how to use git properly.

## Disclaimer
When I first started this, I did this off of only knowledge of programming and absolutely no knowledge of assemblers. This may be very unconventionally programmed (e.g. are you supposed to check for errors before or as you assemble? I went with the latter). 
