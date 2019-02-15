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
    private DBManager DB;
    ObjectInputStream dis;
    ObjectOutputStream ps;
    static Vector<ChatHandler> clients = new Vector<ChatHandler>();
    Socket s;
    static boolean finish;
    boolean found;
    private  String username;
    private Game g;
    private Integer[][] quickGameInitArr={{2,2,2},{2,2,2},{2,2,2}};
    
    
    public ChatHandler( Socket mys,DBManager DB){
        
        try{
        this.DB=DB;
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
    
       // System.out.println("connection stablished");
        while(true)
        {
            try{
                    Request mess =  (Request) dis.readObject();
                  if( mess == null )
                    {
                        throw new Exception("null");
                    }
                  
//                  System.out.println("TEst Test");
//                  System.out.println(mess.getRequestType());
//                  System.out.println(mess.getUserName());
//                  System.out.println(mess.getDistUserName());
//                  System.out.println("Test End");
                    handleRequest(mess);
                    
                    
                    
            }
            catch(Exception e){
                
                try {
                    System.out.println("client logout");
                    ps.close();
                    dis.close();
                    Server.updateStatus(username,0);
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
    
    
    public void handleRequest(Request mess) throws IOException{
        Response r = new Response();
        //Request req = new Request();
        
        
        switch (mess.getRequestType()){
            
           
            
            case "signInSubmit":
            {       found = false;
                    for (int i=0; i< Server.myServ.users.size(); i++) {
                            if( Server.myServ.users.get(i).equals(mess.getUserName())){
                                found = true;
                                if( Server.myServ.passwords.get(i).equals(mess.getPassWord())){       
                                    // return true 
                                    Player currentPlayer = DB.getPlayer(mess.getUserName());
                                    String [] playerData = {currentPlayer.getUsername(), currentPlayer.getScore().toString()};
                                    r.setReponseStatus(true);
                                    r.setReponseType("signin");
                                    r.setUsers(Server.myServ.users);
                                    this.setUserName(mess.getUserName());
                                    r.setStatus(Server.myServ.status);
                                    r.setCurrentPlayerData(playerData);
                                    //this.ps.writeObject(r);
                                    Server.updateStatus(mess.getUserName(),1);
//                                    System.out.println("response sent");
//                                    System.out.println(DB.getPlayer(mess.getUserName()).getScore());
                                    
                                }
                                else{
                                    //return false worng password
                                    
                                    r.setReponseStatus(false);
                                    r.setReponseType("signin");
                                    r.setMessage("wrong password");
                                    //this.ps.writeObject(r);
                                    //System.out.println("response sent");
                                }
                            }
                           // System.out.println("1");
                        }
                        
                        //return false user not found
                        if(!found){
                        
                        r.setReponseStatus(false);
                        r.setReponseType("signin");
                        r.setMessage("user not exist");
                        //this.ps.writeObject(r);
                       // System.out.println("response sent");
                        }
            }
                break;
                
            case "signUpSubmit":
            {
                        //System.out.println("Sign UP request received");
                        boolean userExisted=false;
                        for (int i=0; i< Server.myServ.users.size(); i++)
                        {
                           if( Server.myServ.users.get(i).equals(mess.getUserName())){
                           
                            
                            r.setReponseStatus(false);
                            r.setReponseType("signup");
                            r.setMessage("SignUpFailed");
                           // this.ps.writeObject(r);
                            userExisted=true;
                            break;
                           
                           }
                        }
                        if (!userExisted){
                            r.setReponseStatus(true);
                            r.setReponseType("signup");
                            r.setMessage("SignUp Sucessfully");
                            r.setUsers(Server.myServ.users);
                            r.setStatus(Server.myServ.status);
                            this.setUserName(mess.getUserName());
                            Player p=new Player(mess.getUserName(),mess.getPassWord());
                            boolean s=DB.createNewPlayer(p);
//                            if (s)
//                            System.out.println("Adding to data base successfully");
//                            else
//                                System.out.println("Not added check again");
                                
                            //this.ps.writeObject
                        } 
            }
                
                break;
            case "invite":
            { 
//                System.out.println("invite request is recieved");
//                System.out.println("I am "+mess.getUserName()+" and looking for "+ mess.getDistUserName());
//                System.out.println("WWWWWWWWW");
                
                System.out.println(" I am the server I got "+ mess.getRequestType() + "request from "+ mess.getUserName() + "To "+mess.getDistUserName());
        
                
                
                for(ChatHandler ch: clients)
                {   
                    if (ch.getUserName().equals(mess.getDistUserName()))
                    {  // System.out.println("1sa");
//                        System.out.println("we found");
//                        System.out.println("FFFFFFFFFFFFF");
//                        System.out.println(mess.getUserName());
//                        System.out.println(mess.getDistUserName());
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("invitation request"); 
                        ch.ps.writeObject(r);

                        System.out.println("invitaion sent to client 2");

                        break;
                    }
                }
            }
                break;
            case "invitation response":
            { 
                //System.out.println("invite response is recieved");
                for(ChatHandler ch: clients)
                {
                    if (ch.getUserName().equals(mess.getDistUserName()))
                    {
                        System.out.println(mess.getUserName());
                        System.out.println(mess.getUserName());
                        System.out.println(mess.getUserName());
                        
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("invitation response"); 
                        r.setInvitationReply(mess.getInvitationReply());
                        System.out.println("I am server sending "+ r.getReponseType() +  "to "+mess.getDistUserName());
                        ch.ps.writeObject(r);
                        // Cheking if player 2 accepted the invitation or not 
                         if (r.getInvitationReply())   //if yes we need to instantiate game object
                             g=new Game(mess.getDistUserName(),mess.getUserName(),quickGameInitArr); 
                       // System.out.println("invitaion sent to client 1");
                        break;
                    }
                }
            }
                break;
            case "set username":
            { 
                System.out.println("invite request is recieved");
                this.username = r.getUserName();
            }
        }
        
     if (mess.getRequestType().equals("signInSubmit") || mess.getRequestType().equals("signUpSubmit") || mess.getRequestType().equals("set username")) 
     {
         System.out.println("I am server sending "+ r.getReponseType() +  "to "+r.getDestUsername());
     this.ps.writeObject(r);
     
     }
           
    }

    public String getUserName(){
        return this.username;
    }
    public void  setUserName(String s){
         this.username = s;
    }
 
    public static void close(){
        for( ChatHandler ch : clients){
            try {
                    ch.finish = true;
                    ch.ps.close();
                    ch.dis.close();
                    Server.updateStatus(ch.getUserName(),0);
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


