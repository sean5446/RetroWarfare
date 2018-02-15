# Retro Warfare

Sean Maguire (smaguire)
CS4731       (Dist Comp Sys)
Homework 4   (Retro Warfare)
Date          Dec 2011

![image](https://i.imgur.com/pMweN8t.png)

### NOTES
===============================================================================
** This project was developed in eclipse and built/run in the terminal with 
   the make program **

To move in the client, press w, a, s, d. To shoot, press space.
To exit the client gracefully, press esc, Q, or q

The map is reset after a client scores 20 points (10 kills)


### SYSTEM DEPENDENCIES
===============================================================================
Java x64, JCurses x64 (correct OS build), UDP protocol

** CCCWORK1, CCCWORK2, CCCWORK5 DO NOT have Java x64 (DO NOT USE THEM)
   DO use CCCWORK3, CCCWORK4 **


### STRUCTURE
===============================================================================
Inside the project src directory:
RWClient         - the main client thread
RWClientListener - the listening thread for the client

RWServer         - the main server thread
RWServerListener - the listening thread for the server
RWServerSender   - the sending thread for the server
RWPlayer         - represents a player
RWGame           - represents a Retro Warfare game with players, bullets, map
RWMessage        - inter-thread communication class


### BUILD
===============================================================================
Inside the project root directory use:
"make"           - makes server and client
"make rwserver"  - makes the server
"make rwclient"  - makes the client


### RUN
===============================================================================
Inside the project root directory use:
"make rrwserver" - runs the server
"make rrwclient" - runs the client


