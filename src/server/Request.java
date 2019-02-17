/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
    private int[][] gameBoard;

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
    
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
