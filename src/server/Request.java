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
    
    String requestType;
    String [] msg;
    Player sender;
    String receiverUserName;
    
    public Request(String requestType, String [] msg,Player sender,String receiverUserName){
        this.requestType = requestType;
        this.msg=msg;
        this.sender=sender;
        this.receiverUserName=receiverUserName;
    } 
}
