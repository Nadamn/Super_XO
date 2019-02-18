//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package server;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
///**
// *
// * @author abahaa
// */
//public class Response implements Serializable {
//    
//    private String reponseType;
//    private String message;
//    private boolean reponseStatus;
//    private boolean invitationReplyValue;  // false for decline and true for accept and will be checked only for response type "InvitationStatus"
//    private String userName;
//    private ArrayList<String> users  ;
//    private ArrayList<Integer> status ;
//    private String [] currentPlayerData;
//    private String destinationUsername;
//    
//    public String getMessage(){
//        return this.message;
//    }
//    public void setMessage( String m){
//        this.message = m;
//    }
//    
//    public String getDestUsername(){
//        return this.destinationUsername;
//    }
//    public void setDestUsername( String destinationUsername ){
//        this.destinationUsername = destinationUsername;
//    }
//    
//    
//    public boolean getInvitationReply(){
//       return this.invitationReplyValue;
//    }
//    
//    public void setInvitationReply(boolean x){
//        this.invitationReplyValue=x;
//    }
//    
//    public String [] getCurrentPlayerData(){
//        return this.currentPlayerData;
//    }
//    public void setCurrentPlayerData( String [] currentPlayerData){
//        this.currentPlayerData = currentPlayerData;
//    }
//    
//    public String getReponseType() {
//        return reponseType;
//    }
//
//    public void setReponseType(String reponseType) {
//        this.reponseType = reponseType;
//    }
//
//    public boolean getReponseStatus() {
//        return reponseStatus;
//    }
//
//    public void setReponseStatus(boolean reponseStatus) {
//        this.reponseStatus = reponseStatus;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public ArrayList<String> getUsers() {
//        return users;
//    }
//
//    public void setUsers(ArrayList<String> users) {
//        this.users = users;
//    }
//
//    public ArrayList<Integer> getStatus() {
//        return status;
//    }
//
//    public void setStatus(ArrayList<Integer> status) {
//        this.status = status;
//    }
//   
//    
//    
//    
//}
//




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author abahaa
 */
public class Response implements Serializable {
    
    private String reponseType;
    private String message;
    private boolean reponseStatus;
    private boolean invitationReplyValue;  // false for decline and true for accept and will be checked only for response type "InvitationStatus"
    private String userName;
    private ArrayList<String> users  ;
    private ArrayList<Integer> status ;
    private String [] currentPlayerData;
    private String destinationUsername;
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
     
    
    
    
   
    
    public String getMessage(){
        return this.message;
    }
    public void setMessage( String m){
        this.message = m;
    }
    
    public String getDestUsername(){
        return this.destinationUsername;
    }
    public void setDestUsername( String destinationUsername ){
        this.destinationUsername = destinationUsername;
    }
    
    
    public boolean getInvitationReply(){
       return this.invitationReplyValue;
    }
    
    public void setInvitationReply(boolean x){
        this.invitationReplyValue=x;
    }
    
    public String [] getCurrentPlayerData(){
        return this.currentPlayerData;
    }
    public void setCurrentPlayerData( String [] currentPlayerData){
        this.currentPlayerData = currentPlayerData;
    }
    
    public String getReponseType() {
        return reponseType;
    }

    public void setReponseType(String reponseType) {
        this.reponseType = reponseType;
    }

    public boolean getReponseStatus() {
        return reponseStatus;
    }

    public void setReponseStatus(boolean reponseStatus) {
        this.reponseStatus = reponseStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<Integer> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<Integer> status) {
        this.status = status;
    }
   
    
    
    
}

