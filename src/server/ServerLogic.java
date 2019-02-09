/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author abahaa
 */


public class ServerLogic extends Thread{
    
    public ServerSocket myserver ;
    boolean finish;
    
    public ServerLogic(){
        
        //launch(ar);
        try{
        finish = false;
        start();
        
        }
        catch( Exception e){
            e.printStackTrace();
           }
           
           
    }
    
    
    public void run(){
    
        try{
            
            myserver = new ServerSocket(5005);
            while(!finish){
                System.out.println("hell");
                Socket s = myserver.accept();
                new ChatHandler(s);
            }
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void close(){
        ChatHandler.close();
        finish = true;
    }
}
