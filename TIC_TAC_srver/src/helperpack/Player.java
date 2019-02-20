/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helperpack;


/**
 *
 * @author Nada
 */
public class Player {
    private int id;
    private String username;
    private String password;
    private Integer score =0;
    private int status;
    

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public int getId(){
        return this.id;
    }
   
    public void setId(int id){
        this.id = id ;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassWord(){
        return this.password;
    }
    
    public Integer getScore(){
        return this.score;
    }
    
    public void setScore(Integer score){
        this.score= score;
    }
    
    public int getStatus(){
        return this.status;
    }
    
    public void setStatus(int status){
        this.status= status;
    }
    
    
    
}