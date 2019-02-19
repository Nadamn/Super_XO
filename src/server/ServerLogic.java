/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author abahaa
 */


public class ServerLogic extends Thread{
    
   
    
    public ServerSocket myserver ;
    boolean finish;
    ArrayList<String> users  ;
    ArrayList<Integer> status ;
    ArrayList<String> passwords ;
    
    DBManager db;
    
    public ServerLogic(ArrayList<String> u ,ArrayList<Integer> s ,ArrayList<String> p  ,DBManager db){
        users = u;
        status = s;
        passwords = p;
        this.db=db;
        
        //launch(ar);
   
        finish = false;
       
           
           
    }
    
    public void run(){
    
        try{
            myserver = new ServerSocket(5001);
            while(!finish){
                Socket s = myserver.accept();
                new ChatHandler(s,db);
            }
        }
        catch( Exception e)
        {
            System.out.println("client closed the connection");
        }
    }
    
    public void close(){
        ChatHandler.close();
        finish = true;
    }
}
