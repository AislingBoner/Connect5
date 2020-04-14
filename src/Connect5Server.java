import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Client for a network two-player game of 5-in-a-row (Connect 5).  
 * 
 * This project is an online two-player game of Connect 5 (5-in-a-Row) that uses 
 * client-server architecture implemented by using Java. To Achieve this a new 
 * application-level protocol called Connect5 Protocol (C5P) was created and
 * is entirely plain text. The messages of the C5P are - 
 * Client -> Server   : MOVE <n>  (0 <= n <= 54) , QUIT.   
 * Server -> Client   :  WELCOME <String> (String in {"RED", "YELLOW"}), VALID_MOVE
 *                    , OTHER_PLAYER_MOVED <n>, VICTORY, DEFEAT, TIE, MESSAGE <text>.
 * 
 * @author Aisling Boner
 * @version 1.0
 * 
 * NOTE: Server/Client Theory Adaptions from Deitel and Deitel "Java How to Program" book.
 */
public class Connect5Server {

    /**
     * Main() - Runs the application & pairs up clients that connect together
     * for a game of Connect5.
     */
    public static void main(String[] args) throws Exception {
    
        try (ServerSocket listener = new ServerSocket(5000)) {
            

            System.out.println("Connect 5 Game Server is Running");
            System.out.println("Listening on IP Address: "+ listener.getInetAddress());
            System.out.println("Listening on Port: "+ listener.getLocalSocketAddress());
            System.out.println("Waiting on Connections... ");

            while (true) {
                Connect5Game game = new Connect5Game();
                Connect5Game.Player player1 = game.new Player(listener.accept(), "RED");
                System.out.println("Player1 has connected.");
                Connect5Game.Player player2 = game.new Player(listener.accept(), "YELLOW");
                System.out.println("Player2 has connected.");
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        }
    }
}

/**
 * Connect5Game Class - a two-player game of connect5 for 2 paired clients
 * to play. It consists of the general structure and setUp of the game 
 * including its rules.
 */
class Connect5Game {

    /**
      * Game board consists of 54 squares (9 columns * 6 rows). Using a simple
      * array to depict this we can reference player moves and show this on the user interface
      * i.e. empty squares or chosen squares selected by the player as done in the original game.  
      * If null, the corresponding square has not been chosen, otherwise the array cell stores 
      * a reference to the player that selected it.
      */
     private Player[] board = {
         null, null, null, null, null, null, null, null, null, 
         null, null, null, null, null, null, null, null, null,
         null,null, null, null, null, null, null, null, null, 
         null, null, null, null, null, null, null, null, null, 
         null,null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null };
  
     Player currentPlayer;
 
    /**
      * isWinner() - States the situations where a player has won.
      * @return The current state of the board, determines if a player has won
      */
     public boolean isWinner() {
          
        // 5 Accross - HorizontalCheck
          for (int col = 0 ; col< 10-5 ; col++){ //column
             for (int row = 0 ; row < 54 ; row+=9){ //row

                 if ( board[row + col]!= null &&
                  board[row +col] == board[row +col+1] && 
                  board[row +col] == board[row+col+2] && 
                  board[row +col] ==  board[row+col+3] && 
                  board[row +col] ==  board[row+col+4]){
                    System.out.println("Horizontal win.");
                    return true;
                 
                 }
             }
         }
         //5 Down - VerticalCheck (Sort Issue)
         for (int row = 0 ; row< 27; row+=9){
             for (int col = 0 ; col < 10  ; col++){

                 if ( board[row  + col]!= null && 
                 board[row +col] == board[row +9 +col] && 
                 board[row +col] == board[row+(18) +col] && 
                 board[row +col] ==  board[row+(27) +col] && 
                 board[row +col] ==  board[row+(36) +col]){
                    System.out.println("Vertical win.");
                    return true;
                 }
             }
         }
         //5 Diagonal - AscendingDiagonalCheck 
         for (int row = 27 ; row< 54 ; row+=9){
             for (int col = 0 ; col <5 ; col++){

                 if (    board[row + col]!= null && 
                 board[row +col]== board[(row-9) +col+1] && 
                 board[(row-9) +col+1] == board[row-18 +col+2] && 
                 board[(row-18) +col+2] ==  board[(row-27) +col+3] && 
                 board[(row-27) +col+3] ==  board[(row-36) +col+4] ){
                    System.out.println("Accending Diagonal.");
                    return true;
                 }
             }
         }
         //5 Diagonal - DescendingDiagonalCheck
         for (int row = 27 ; row< 54 ; row+=9){
             for (int col = 4 ; col < 9; col++){
                 if (    board[row  + col]!= null && 
                 board[row +col]== board[(row-9) +col-1 ] && 
                 board[(row-9) +col-1 ] == board[(row-18) +col-2] && 
                 board[(row-18) +col-2] ==  board[(row-27) +col-3] && 
                 board[(row-27) +col-3] ==  board[(row-36) +col-4]){
                    System.out.println("Descending Diagonal."); 
                    return true;
                 }
             }
         }
         return false;
     }
 
     /**
      * boardFilledUp() - Checks if the board squares are 
      * full
      * @return If there is or isnt empty squares.
      */
     public boolean boardFilledUp() {
         for (int i = 0; i < board.length; i++) {
             if (board[i] == null) {
                 return false;
             }
         }
         return true;   
     }
 
     /**
      * moveCheck() - method is called by each player thread when
      * they make a move. This method checks to see if the move is legal 
      * and if so updates on both clients GUIs.
      */
     public synchronized int moveCheck(int location, Player player){
         int minlocation = (location % 9)+9*5;

         for(int i = minlocation ; i >= location; i-= 9)
             if (player == currentPlayer && board[i] == null) {
                 board[i] = currentPlayer;
                 currentPlayer = currentPlayer.opponent;
                 currentPlayer.opponentMoves(i);
                 return i;
             }
         return -1;

     } 
 
     /**
      * Player class - extends helper threads for this multithreaded
      * server game. Allows for text (read & write) communication with
      * the client. Consists of a socket with I/O streams.  
      */
     class Player extends Thread {
         String mark;//A Player is identified by a character mark which is either '1' or '2'.
         Player opponent;
         Socket socket;
         BufferedReader input;
         PrintWriter output;
 
        /**
          * Player constructor - this constructs a handler thread for a given socket
          * and mark. It then initializes the stream fields & displays the initial messages for the
          * players determining the game state.
          */
         public Player(Socket socket, String mark) {
             this.socket = socket;
             this.mark = mark;
             try {
                 input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
                 output = new PrintWriter(socket.getOutputStream(), true);
                 output.println("WELCOME " + mark);
                 output.println("MESSAGE Waiting for your opponent to Connect!");
                 
             } catch (IOException e) {
                 System.out.println("Player Left: " + e);
                 
             }
         }
 
         /**
          * Sets opponent Player.
          */
         public void setOpponent(Player opponent) {
             this.opponent = opponent;
             
         }
 
         /**
          * opponentMoves() - Handles messages from the 
          * opponent & Checks if the opponent player has won or not
          * and also checks if the board is full.
          */
         public void opponentMoves(int location) {
             output.println("OPPONENT_MOVED " + location);
             output.println(
                 isWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
         }
 
         /**
          * run() - Run method of this thread only starts once the two
          * Client players have connected.
          */
         public void run() {
             try {
                 output.println("MESSAGE All players connected");
           
                 if (mark.equals("RED")) {//Tells the 1st connected Client that it is their move.
                     output.println("MESSAGE Your move");
                 }
 
                 
                 while (true) {// Repeatedly get Client commands & Processes them.
                     String command = input.readLine();
                     if (command.startsWith("MOVE")) { 
                         int location = Integer.parseInt(command.substring(5));
                         int validlocation = moveCheck(location, this);
                         if (validlocation!= -1) {
                             output.println("VALID_MOVE"+validlocation);
                             output.println(isWinner() ? "VICTORY"
                                          : boardFilledUp() ? "TIE"
                                          : "");
                         } else {
                             output.println("MESSAGE Wait your Turn");
                         }
                     } else if (command.startsWith("QUIT")) {
                         System.out.println("Player Exited. Game Over.");
                         return;
                     }
                 }
             } catch (IOException e) {
                 System.out.println("Player left: " + e);
                 
             } finally {
                 
                 if (opponent != null && opponent.output != null) {
                     opponent.output.println("OTHER_PLAYER_LEFT");
                 }
                 try {
                     socket.close();
                     System.out.println("Server Side Connection Closed. ");
                     
 
                 } catch (IOException e) 
                 {System.out.println("Player left: " + e);
                 System.exit(1);
                }
                 
             }
         }
     }
 }
 
 

    


