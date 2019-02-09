/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Nada
 */
public class Player {
    private int id;
    private String username;
    private String password;
    private String name;
    private int score;
    private int status;
    

    public Player(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassWord(){
        return this.password;
    }
    
    
    public String getName(){
        return this.name;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public void setScore(int score){
        this.score= score;
    }
    
    public int getStatus(){
        return this.status;
    }
    
    public void setStatus(int status){
        this.status= status;
    }
    
    
    
}
