# Connect5
Connect5 (5-in-a-Row) This project is an online two-player game of Connect 5 (5-in-a-Row) that uses client-server architecture implemented by using Java.  5-in-a-Row, a variation of the famous Connect Four game, is a two-player connection game in which the players first choose a colour (Red or Yellow) and then take turns dropping the coloured discs from the top into a nine-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of five of one's own discs.   

Implementation:  The game is coded on the game server 'Connect5Server' using the created protocol called Connect5 Protocol (C5P). This protocol consists entirely of plain text messages to describe the ongoings of the game. The messages of the C5P include: WELCOME, MOVE, QUIT, VALID_MOVE,OTHER_PLAYER_MOVED, VICTORY, DEFEAT, TIE &amp; MESSAGE. The Client uses a Graphical User Interface (GUI) (Written in Visual Studio Code based on Swing &amp; Java) opened from the Terminal allowing two client threads to join and play concurrently a game of Connect5 on a single server.   

Instructions for setting up the Connect 5 Game:  

1. Open the terminal and change directories until you are in the Connect5 game folder. 

2. Run Server side connection hosting the game by entering the command into the terminal.. java Connect5Server.java  

3. Open up two additional terminals for each of the clients to be connected.  

4. Run Player1's Client side connection in one of the terminals with the command into the terminal...    
java Connect5Client.java    and then the player enters their name in the console.   

5. Run Player2's Client side connection in the other terminal with the command into the terminal... 
java Connect5Client.java    and then the player enters their name in the console.  

6. After each of the Clients have entered their name into the console for setup the GUI opens and the game of Connect5 commence in the GUIs as described above in the description of the game. Instructions throughout the game are shown on screen in an easy to follow manner.

7. If You want a rematch after the game is complete select yes to the rematch. Following this re-name the client players again as seen in steps 4 and 5 and the GUI will reload and the new game will commence.
