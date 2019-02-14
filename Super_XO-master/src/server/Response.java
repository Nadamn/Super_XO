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
    private String userName;
    private ArrayList<String> users  ;
    private ArrayList<Integer> status ;
    
    public String getMessage(){
        return this.message;
    }
    public void setMessage( String m){
        this.message = m;
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

