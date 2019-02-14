/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Mostafa
 */

public class Game {
    private String p1Name;  //x    we may need to change this to be player1 name of type string only 
    private String p2Name;  //o
    private Integer[][] gameBoard;
    private Integer lastMovePlayer=0;// This will track which player played last move
                                     // (odd numbers for player 1(x)  and even numbers for player2(o))
                                     // will be increased for each requet of type move by 1
                                     // for example if lastmovePlayer is 3 then player1(x) played last move and now it should be player2 (o) turn
    
    private Integer lastMoveRow;
    private Integer lastMoveCol;
    
    private String gameState;  // 1wins  2wins  tie    that's all the three cases 
    
    
    
//     Steps :
//       1- increase last move player
//       2- set lastmoveRow and lastmovecol from the external request of type move
//       3- adjust game board using adjust gameboard method  
//       4- check for winner using checkWinner after every move
//       5- if there are winners or tie game update gamestate attribute so the external handler can make the corresponding response  
//    
    public Game(String p1Name,String p2Name, Integer[][]gameBoard){    // Player 1 X  player 2 O
        this.p1Name=p1Name;
        this.p2Name=p2Name;
        this.gameBoard=gameBoard;
    }
    
//    public void setPlayer1(Player p1){
//        this.p1 = p1;
//    }
    
//    public Player getPlayer1(){
//        return this.p1;
//    }
    
//    public void setPlayer2(Player p2){
//        this.p2 = p2;
//    }
    
//    public Player getPlayer2(){
//        return this.p2;
//    }
    
    public void setGameBoard(Integer [][] gameBoard){
        this.gameBoard = gameBoard;
    }
    
    public Integer [][] getGameBoard(){
        return this.gameBoard;
    }
    
    
    public void setLastMoveRow(Integer lastMoveRow){this.lastMoveRow =lastMoveRow;}
    public Integer getLastMoveRow(){return this.lastMoveRow;}
    
    public void setLastMoveCol(Integer lastMoveCol){this.lastMoveCol = lastMoveCol;}
    public Integer getLastMoveCol(){return this.lastMoveCol;}
    
    
//    public void setCurrentPlayer(Integer currentPlayer){
//        this.currentPlayer = currentPlayer;
//    }
//    
//    public Integer getCurrentPlayer(){
//        return this.currentPlayer;
//    }
    
    public void adjustGameBoard(){
    
        if (lastMovePlayer % 2 !=0)  // player 1 (X)
           gameBoard[lastMoveRow][lastMoveCol]=1;
        else                         // player 2 (O)
            gameBoard[lastMoveRow][lastMoveCol]=0;
        // and by default all other buttons on state 2 (not clicked yet)
        
    
    
    }
   
    
    
    public void checkWinner(){                // will be called after each increase in lastmove player
                                               // dummy method for 3*3 boards only
        
        
        // Check for horizontal
          if ( (gameBoard[lastMoveRow][0] == gameBoard[lastMoveRow][2])&& (gameBoard[lastMoveRow][1] == gameBoard[lastMoveRow][2]) && (gameBoard[lastMoveRow][0] == gameBoard[lastMoveRow][lastMoveCol]) )
          {
               if (gameBoard[lastMoveRow][lastMoveCol]==1)
                   gameState="1wins";
               if (gameBoard[lastMoveRow][lastMoveCol]==0)
                   gameState="2wins";
          
          }
                        
        
        // Check for vertical 
        
          if ( (gameBoard[0][lastMoveCol] == gameBoard[2][lastMoveCol])&& (gameBoard[1][lastMoveCol] == gameBoard[2][lastMoveCol]) && (gameBoard[0][lastMoveCol] == gameBoard[lastMoveRow][lastMoveCol]) )
          {
               if (gameBoard[lastMoveRow][lastMoveCol]==1)
                   gameState="1wins";
               if (gameBoard[lastMoveRow][lastMoveCol]==0)
                   gameState="2wins";
          
          }
        
        
        
        // Check for diagonal (check later)
        
        
        
        // Check for tie case 
        if (lastMovePlayer==9  && (!gameState.equals("1wins")) && (!gameState.equals("2wins"))  )
               gameState="tie";
            
            
        
        
        
    
    
    
    }
    
}
 