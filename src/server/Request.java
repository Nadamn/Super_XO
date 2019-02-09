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
public class Request {
    
    private String requestType;
    private String [] msg;
    private Player sender;
    private String receiverUserName;
    
    public Request(String requestType, String [] msg,Player sender,String receiverUserName){
        this.requestType = requestType;
        this.msg=msg;
        this.sender=sender;
        this.receiverUserName=receiverUserName;
    } 
    
    public String getRequestType(){
        return this.requestType;
    }
   
    public String [] getMsg(){
        return this.msg;
    }
   
    public Player getSender(){
        return this.sender;
    }
    
    public String getRecieverUserName(){
        return this.receiverUserName;
    }   
}
