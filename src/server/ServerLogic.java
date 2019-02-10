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
    
    public ServerLogic(ArrayList<String> u ,ArrayList<Integer> s ,ArrayList<String> p  ){
        users = u;
        status = s;
        passwords = p;
        
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
            System.out.println("hell1");
            myserver = new ServerSocket(5001);
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
