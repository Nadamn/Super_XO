/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Circle;

/**
 *
 * @author abahaa
 */

    

class ChatHandler extends Thread {

    ObjectInputStream dis;
    ObjectOutputStream ps;
    static Vector<ChatHandler> clients = new Vector<ChatHandler>();
    Socket s;
    static boolean finish;
    public ChatHandler( Socket mys){
        
        try{
        s = mys;
        dis = new ObjectInputStream(mys.getInputStream());
        ps = new ObjectOutputStream(mys.getOutputStream());
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
                    Request mess = (Request) dis.readObject();
                    if( mess == null )
                    {
                        throw new Exception("null");
                    }
                    
                    // check mess type by switch case 
                    // case "signin"
                    // 
                    if( mess.getMsg().equals("signin") ){
                        for (int i=0; i< Server.myServ.users.size(); i++) {
                            if( Server.myServ.users.get(i).equals(mess.getMsg()[0])){
                                if( Server.myServ.passwords.get(i).equals(mess.getMsg()[1])){
                                        
                                    // return true 
                                }
                                else{
                                    //return false worng password
                                }
                            }
                            
                        }
                        
                        //return false user not found
                        
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
                    //sendToAll("client removed");
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void sendToAll(Request mess) throws IOException{
        
        for( ChatHandler ch : clients){
            System.out.println("sent");
            ch.ps.writeObject(mess);
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


