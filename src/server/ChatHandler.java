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
    //private Game g;
    private int[][] quickGameInitArr={{2,2,2},{2,2,2},{2,2,2}};
    
    
    
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
                    System.out.println("Received request of type "+mess.getRequestType());
                  if( mess == null )
                    {
                        System.out.println("mess = null");
                        throw new Exception("null");
                    }
                   
                    handleRequest(mess);
                   
                   
                    
            } catch (IOException ex) {
                Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (java.lang.Exception ex) {
                Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            catch(Exception e){
                
                try {
                    System.out.println("client logout");
                    ps.close();
                    dis.close();
                    Server.updateStatus(username,0);
                    clients.remove(this);
                    
                    Response r2 =  new Response();
                    int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                    r2.setReponseType("status update");
                    r2.setUsers(Server.myServ.users);                       
                    r2.setStatus(arr);
                    for( ChatHandler ch2 : clients)
                        {
                            ch2.ps.writeObject(r2);                              
                        }
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
                    for (int i=0; i< Server.myServ.users.size(); i++) 
                        {
                            if( Server.myServ.users.get(i).equals(mess.getUserName()))
                            {
                                found = true;
                                if( Server.myServ.passwords.get(i).equals(mess.getPassWord()))
                                {       
                                    // return true 
                                    Player currentPlayer = DB.getPlayer(mess.getUserName());
                                    String [] playerData = {currentPlayer.getUsername(), currentPlayer.getScore().toString()};
                                    r.setReponseStatus(true);
                                    r.setReponseType("signin");
                                    r.setUsers(Server.myServ.users);
                                    this.setUserName(mess.getUserName());
                                    
                                    int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                                    
                                    r.setStatus(arr);
                                    r.setCurrentPlayerData(playerData);
                                    
                                    //  update status in server
                                    Server.updateStatus(mess.getUserName(),1);
//                                    System.out.println("response sent");
//                                    System.out.println(DB.getPlayer(mess.getUserName()).getScore());

                                    // update clients with new updates
                                    Response r2 =  new Response();
                                    arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                                    r2.setReponseType("status update");
                                    r2.setUsers(Server.myServ.users);                       
                                    r2.setStatus(arr);
                                    for( ChatHandler ch2 : clients)
                                        {
                                            System.out.println("inner loop");
                                            System.out.println(r2.getStatus());
                                            if( !ch2.username.equals(mess.getUserName()))
                                                {
                                                    System.out.println("sent loop");
                                                    ch2.ps.writeObject(r2);
                                                } 
                                        }
                                }
                                else
                                    {
                                        //return false worng password

                                        r.setReponseStatus(false);
                                        r.setReponseType("signin");
                                        r.setMessage("wrong password");
                                        //this.ps.writeObject(r);
                                        //System.out.println("response sent");
                                    }
                            }
                           
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
                           
                            
                            r.setReponseStatus(false);///////////////////
                            r.setReponseType("signup");
                            r.setMessage("SignUpFailed");
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
                            String [] playerData = {mess.getUserName(),"0"};
                            r.setCurrentPlayerData(playerData);
                            
                            Player p=new Player(mess.getUserName(),mess.getPassWord());
                            boolean s=DB.createNewPlayer(p);
                          
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
                        Server.updateStatus(mess.getUserName(),2);
                        Server.updateStatus(mess.getDistUserName(),2);
                        ch.ps.writeObject(r);


                        Response r2 =  new Response();
                        int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        r2.setReponseType("status update");
                        r2.setUsers(Server.myServ.users);                       
                        r2.setStatus(arr);
                        for( ChatHandler ch2 : clients)
                            {
                                System.out.println(r2.getStatus());
                                if( !ch2.username.equals(mess.getUserName()) && !ch2.username.equals(mess.getDistUserName()))
                                    {
                                        System.out.println("sent loop");
                                        ch2.ps.writeObject(r2);
                                    } 
                            }
                        
                        System.out.println("invitaion sent to client 2");


                        break;
                    }
                }
            }
                break;

                
            case "cancel invitation":
            { 
            
                System.out.println(" I am the server I got "+ mess.getRequestType() + "request from "+ mess.getUserName() + "To "+mess.getDistUserName());
            
                for(ChatHandler ch: clients)
                {   
                    if (ch.getUserName().equals(mess.getDistUserName()))
                    {  
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("cancel invitation"); 
                        ch.ps.writeObject(r);

                        System.out.println("cancel invitation sent to client 2");

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

                        
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("invitation response"); 
                        r.setInvitationReply(mess.getInvitationReply());
                        System.out.println("I am server sending "+ r.getReponseType() +  "to "+mess.getDistUserName());

                        ch.ps.writeObject(r);
                        // Cheking if player 2 accepted the invitation or not 
                        if (r.getInvitationReply())   //if yes we need to instantiate game object
                            {
                                g=new Game(mess.getDistUserName(),mess.getUserName(),quickGameInitArr); 
                                Server.updateStatus(mess.getUserName(),2);
                                Server.updateStatus(mess.getDistUserName(),2);
                            }
                        else
                            {
                                Server.updateStatus(mess.getUserName(),1);
                                Server.updateStatus(mess.getDistUserName(),1);
                            }

                        Response r2 =  new Response();
                        int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        r2.setReponseType("status update");
                        r2.setUsers(Server.myServ.users);                       
                        r2.setStatus(arr);
                        for( ChatHandler ch2 : clients)
                            {
                                System.out.println(r2.getStatus());
                                if( !ch2.username.equals(mess.getUserName()) && !ch2.username.equals(mess.getDistUserName()))
                                    {
                                        System.out.println("sent loop");
                                        ch2.ps.writeObject(r2);
                                    } 
                            }

                        break;
                    }
                }
            }
                break;
                
                
              case "moveToO":
              {
//                
                Response gameRes=new Response();
                gameRes.setReponseType("receiveInO");
                gameRes.setGameBoard(mess.getGameBoard());
                gameRes.setPlayer1Name(mess.getPlayer1Name());
                gameRes.setPlayer2Name(mess.getPlayer2Name());
                System.out.println("Got the first move");
                  for(ChatHandler ch: clients){
                     // if (ch.getUserName().equals(g.getPlayer2Name()))
                     if (ch.getUserName().equals(mess.getPlayer2Name()))
                      {
                          ch.ps.writeObject(gameRes);
                          
                          if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="1wins"){
                              DB.increaseScore(mess.getPlayer1Name()); // Increasing score
                               Response result = new Response();
                                 result.setReponseType("win");
                                 this.ps.writeObject(result); 
                                 
                                 result.setReponseType("lose");
                                 ch.ps.writeObject(result);
                                 
                                 
                                 
                          }
                          else if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="2wins")
                          {
                              DB.increaseScore(mess.getPlayer2Name()); // Increasing score
                               Response result = new Response();
                                 result.setReponseType("win");
                                 ch.ps.writeObject(result);  
                                 
                                 result.setReponseType("lose");
                                 this.ps.writeObject(result);
                          }
                           else if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="tie")
                          {
                               Response result = new Response();
                                 result.setReponseType("TIE");
                                 ch.ps.writeObject(result);
                                 this.ps.writeObject(result);
                          }
                          
                          
                           
                          
                          break;
                      }
                  }
            }
            break;
            
            case "moveToX":{
                //g.increaseLastMovePlayer();
                //g.setGameBoard(mess.getGameBoard());
                Response gameRes=new Response();
                gameRes.setReponseType("receiveInX");
                gameRes.setPlayer1Name(mess.getPlayer1Name());
                gameRes.setPlayer2Name(mess.getPlayer2Name());
                gameRes.setGameBoard(mess.getGameBoard());     
                  for(ChatHandler ch: clients){
                      if (ch.getUserName().equals(mess.getPlayer1Name()))
                      {
                          ch.ps.writeObject(gameRes);
                          
                          
                          if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="1wins"){
                              
                              DB.increaseScore(mess.getPlayer1Name()); // Increasing score
                               Response result = new Response();
                                 result.setReponseType("win");
                                 ch.ps.writeObject(result); 
                                 
                                 result.setReponseType("lose");
                                 this.ps.writeObject(result);
                                 
                          }
                          else if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="2wins")
                          {
                              DB.increaseScore(mess.getPlayer2Name()); // Increasing score
                               Response result = new Response();
                                 result.setReponseType("win");
                                 this.ps.writeObject(result); 
                                 
                                 result.setReponseType("lose");
                                 ch.ps.writeObject(result);
                          }
                           else if (checkWinner(mess.getGameBoard(),mess.getCurrentMoveRow(),mess.getCurrentMoveCol())=="tie")
                          {
                               Response result = new Response();
                                 result.setReponseType("TIE");
                                 ch.ps.writeObject(result);
                                 this.ps.writeObject(result);
                          }
                          
                          
                          
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
    
    
    
    
    
    
    public String checkWinner(int[][]board,int lastMoveRow,int lastMoveCol){                // will be called after each increase in lastmove player
                                                                             // dummy method for 3*3 boards only
                                                                             
         String gameState="Nothing Yet";
         int NumofMoves=0;
         
         
         for(int i=0;i<3;i++)
           for(int j=0;j<3;j++)
               if(board[i][j]==1 || board[i][j]==0)
                   NumofMoves++;
         
        // Check for horizontal
          if ( (board[lastMoveRow][0] == board[lastMoveRow][2])&& (board[lastMoveRow][1] == board[lastMoveRow][2]) && (board[lastMoveRow][0] == board[lastMoveRow][lastMoveCol]) )
          {
               if (board[lastMoveRow][lastMoveCol]==1)
                 return  gameState="1wins";
               if (board[lastMoveRow][lastMoveCol]==0)
                 return  gameState="2wins";
          
          }
                        
        
        // Check for vertical 
        
          if ( (board[0][lastMoveCol] == board[2][lastMoveCol])&& (board[1][lastMoveCol] == board[2][lastMoveCol]) && (board[0][lastMoveCol] == board[lastMoveRow][lastMoveCol]) )
          {
               if (board[lastMoveRow][lastMoveCol]==1)
                 return gameState="1wins";
               if (board[lastMoveRow][lastMoveCol]==0)
                 return  gameState="2wins";
          
          }
        
        
        
        // Check for diagonal (check later)
        
        
        
        // Check for tie case 
        if (NumofMoves==9  && (!gameState.equals("1wins")) && (!gameState.equals("2wins"))  )
               return gameState="tie";
            
            
        
        
        
    return gameState;
    
    
    }
    

 
    
    
    
    
}


