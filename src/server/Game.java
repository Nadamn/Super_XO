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
    private Player p1;
    private Player p2;
    private Integer[] gameBoard;
    private Integer currentPlayer;
    
    public Game(Player p1,Player p2, Integer[]gameState){
        this.p1=p1;
        this.p2=p2;
        this.gameBoard=gameState;
    }
    
    public void setPlayer1(Player p1){
        this.p1 = p1;
    }
    
    public Player getPlayer1(){
        return this.p1;
    }
    
    public void setPlayer2(Player p2){
        this.p2 = p2;
    }
    
    public Player getPlayer2(){
        return this.p2;
    }
    
    public void setGameBoard(Integer [] gameBoard){
        this.gameBoard = gameBoard;
    }
    
    public Integer [] getGameBoard(){
        return this.gameBoard;
    }
    
    
    public void setCurrentPlayer(Integer currentPlayer){
        this.currentPlayer = currentPlayer;
    }
    
    public Integer getCurrentPlayer(){
        return this.currentPlayer;
    }
}
