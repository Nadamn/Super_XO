//

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helperpack;


import java.io.Serializable;

/**
 *
 * @author Mostafa
 */
public class Request implements Serializable {
    
    private String requestType;
    private String userName;               // for the request sender
    private String passWord;               // for the request sender
    private Integer currentPlay;
    private String chatMsg;
    private boolean Reply;
    private String destUserName;
    
    private Integer currentMoveRow;
    private Integer currentMoveCol;
  
   
    private int[][] gameBoard;
    private String player1Name;
    private String player2Name;
  
    
    
    
    public void setPlayer1Name(String x){  
        this.player1Name=x;
    }
    
    public String getPlayer1Name(){  
     return this.player1Name;
    }
    
     public void setPlayer2Name(String x){  
        this.player2Name=x;
    }
    
    public String getPlayer2Name(){  
     return this.player2Name;
    }
    
    
    public void setGameBoard(int[][] x){
    this.gameBoard=x;
    }
    
    //////////////////////
    
    public int[][] getGameBoard(){
    return this.gameBoard;
    }
    
    
    public void setCurrentMoveRow(Integer x){this.currentMoveRow=x;}
    public Integer getCurrentMoveRow(){return this.currentMoveRow;}
    
    public void setCurrentMoveCol(Integer x){this.currentMoveCol=x;}
    public Integer getCurrentMoveCol(){return this.currentMoveCol;}
    
    
    public void setInvitationReply(boolean rep){ this.Reply = rep;}
    public boolean getInvitationReply(){ return this.Reply ;}
    
    public void setDistUserName(String userName){this.destUserName=userName;}
    public String getDistUserName(){return this.destUserName;}
    
    
    public void setRequestType(String type){this.requestType=type;}
    public String getRequestType(){return this.requestType;}
    
    public void setUserName(String userName){this.userName=userName;}
    public String getUserName(){return this.userName;}
    
    public void setPassWord(String passWord){this.passWord=passWord;}
    public String getPassWord(){return this.passWord;}
    
    public void setCurrentPlay(Integer move){this.currentPlay=move;}
    public Integer getCurrentPlay(){return this.currentPlay;}
    
    public void setChatMsg(String msg){this.chatMsg=msg;}
    public String getChatMsg(){return this.chatMsg;}   
}
