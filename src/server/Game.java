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
    Player p1;
    Player p2;
    Integer[] gameBoard;
    Integer currentPlayer;
    
    public Game(Player p1,Player p2,Integer[]gameState){
          
        this.p1=p1;
        this.p2=p2;
        this.gameBoard=gameState;
    }
        
}
