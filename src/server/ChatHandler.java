/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abahaa
 */

    

class ChatHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static Vector<ChatHandler> clients = new Vector<ChatHandler>();
    Socket s;
    static boolean finish;
    public ChatHandler( Socket mys){
        
        try{
        s = mys;
        dis = new DataInputStream(mys.getInputStream());
        ps = new PrintStream(mys.getOutputStream());
        clients.add(this);
        finish = false;
        start();
        
        }
        catch( Exception e){
            e.printStackTrace();
           }
        
        
        
    }
    
    
    public void run(){
    
        System.out.println("connection stablished");
        while(!finish)
        {
            try{
                    String mess = dis.readLine();
                    if( mess == null )
                    {
                        throw new Exception("null");
                    }
                    sendToAll(mess);
                    //System.out.println("send been called");
            }
            catch(Exception e){
                
                try {
                    System.out.println("client logout");
                    ps.close();
                    dis.close();
                    clients.remove(this);
                    s.close();
                    sendToAll("client removed");
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void sendToAll(String mess){
        
        for( ChatHandler ch : clients){
            ch.ps.println(mess);
        } 
    }

    private void Exception(String anull) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void close(){
        for( ChatHandler ch : clients){
            try {
                    ch.finish = true;
                    ch.ps.close();
                    ch.dis.close();
                    clients.remove(ch);
                    ch.s.close();
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
        } 
        System.out.println("all users removed");
    }
    
    
}


