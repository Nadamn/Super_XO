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
    private String username;
    //private Game g;
    private int[][] quickGameInitArr = {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}};

    public ChatHandler(Socket mys, DBManager DB) {

        try {
            this.DB = DB;
            s = mys;
            dis = new ObjectInputStream(mys.getInputStream());
            ps = new ObjectOutputStream(mys.getOutputStream());
            clients.add(this);
            finish = false;
            start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (true) {
            try {
                Request mess = (Request) dis.readObject();
                System.out.println("Received request of type " + mess.getRequestType());
                if (mess == null) {
                    System.out.println("mess = null");
                    throw new Exception("null");
                }

                handleRequest(mess);

            } catch (Exception e) {

                try {
                    System.out.println("client logout");
                    ps.close();
                    dis.close();
                    Server.updateStatus(username, 0);
                    clients.remove(this);

                    Response r2 = new Response();
                    int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                    String[] names = Server.myServ.users.toArray(new String[Server.myServ.users.size()]);
                    int[] sco = Server.myServ.scores.stream().mapToInt(j -> j).toArray();
                    r2.setScores(sco);
                    r2.setReponseType("status update");
                    r2.setUsers(names);
                    r2.setStatus(arr);
                    for (ChatHandler ch2 : clients) {
                        ch2.ps.writeObject(r2);
                    }
                    s.close();
                    //sendToAll("client removed");
                    break;
                } catch (IOException ex) {
                    System.out.println("failed to close resourcers");
                }
            }

        }
    }

    public void sendToAll(Request mess) throws IOException {

        for (ChatHandler ch : clients) {
            System.out.println("sent");
            ch.ps.writeObject(mess);
        }
    }

    public void handleRequest(Request mess) throws IOException {
        Response r = new Response();
        //Request req = new Request();

        switch (mess.getRequestType()) {

            case "signInSubmit": {
                found = false;
                for (int i = 0; i < Server.myServ.users.size(); i++) {
                    if (Server.myServ.users.get(i).equals(mess.getUserName())) {
                        found = true;
                        if (Server.myServ.passwords.get(i).equals(mess.getPassWord())) {

                            Player currentPlayer = DB.getPlayer(mess.getUserName());
                            String[] playerData = {currentPlayer.getUsername(), currentPlayer.getScore().toString()};
                            r.setReponseStatus(true);
                            r.setReponseType("signin");
                            int[] sco = Server.myServ.scores.stream().mapToInt(j -> j).toArray();
                            r.setScores(sco);
                            this.setUserName(mess.getUserName());
                            String[] names = Server.myServ.users.toArray(new String[Server.myServ.users.size()]);
                            int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                            r.setUsers(names);
                            r.setStatus(arr);
                            r.setCurrentPlayerData(playerData);

                            //  update status in server
                            Server.updateStatus(mess.getUserName(), 1);

                            // update clients with new updates
                            Response r2 = new Response();
                            arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();

                            r2.setReponseType("status update");
                            r2.setUsers(names);
                            r2.setStatus(arr);
                            r2.setScores(sco);
                            for (ChatHandler ch2 : clients) {

                                //System.out.println(r2.getStatus());
                                if (!ch2.username.equals(mess.getUserName())) {
                                    ch2.ps.writeObject(r2);
                                }
                            }
                        } else {
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
                if (!found) {

                    r.setReponseStatus(false);
                    r.setReponseType("signin");
                    r.setMessage("user not exist");
                    //this.ps.writeObject(r);
                    // System.out.println("response sent");
                }
            }
            break;

            case "signUpSubmit": {
                //System.out.println("Sign UP request received");
                boolean userExisted = false;
                for (int i = 0; i < Server.myServ.users.size(); i++) {
                    if (Server.myServ.users.get(i).equals(mess.getUserName())) {

                        r.setReponseStatus(false);///////////////////
                        r.setReponseType("signup");
                        r.setMessage("SignUpFailed");
                        userExisted = true;
                        break;

                    }
                }
                if (!userExisted) {
                    r.setReponseStatus(true);
                    r.setReponseType("signup");
                    r.setMessage("SignUp Sucessfully");

//                            r.setStatus(Server.myServ.status);
                    this.setUserName(mess.getUserName());
                    String[] playerData = {mess.getUserName(), "0"};
                    r.setCurrentPlayerData(playerData);

                    Player p = new Player(mess.getUserName(), mess.getPassWord());
                    boolean s = DB.createNewPlayer(p);

                    //let users and status in server load from db 
                    Server.updateUsers(p);

                    int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                    String[] names = Server.myServ.users.toArray(new String[Server.myServ.users.size()]);
                    int[] sco = Server.myServ.scores.stream().mapToInt(j -> j).toArray();
                    r.setScores(sco);
                    r.setStatus(arr);
                    r.setUsers(names);

                    Response r2 = new Response();

                    r2.setReponseType("status update");
                    r2.setUsers(names);
                    r2.setStatus(arr);
                    r2.setScores(sco);
                    for (ChatHandler ch2 : clients) {
                        //System.out.println(r2.getStatus());
                        if (!ch2.username.equals(mess.getUserName())) {
                            ch2.ps.writeObject(r2);
                        }
                    }

                }
            }

            break;
            case "invite": {

                //System.out.println(" I am the server I got "+ mess.getRequestType() + "request from "+ mess.getUserName() + "To "+mess.getDistUserName());
                for (ChatHandler ch : clients) {
                    if (ch.getUserName().equals(mess.getDistUserName())) {
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("invitation request");
                        Server.updateStatus(mess.getUserName(), 2);
                        Server.updateStatus(mess.getDistUserName(), 2);
                        ch.ps.writeObject(r);

                        Response r2 = new Response();
                        int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        String[] names = Server.myServ.users.toArray(new String[Server.myServ.users.size()]);
                        arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        int[] sco = Server.myServ.scores.stream().mapToInt(j -> j).toArray();
                        r2.setScores(sco);
                        r2.setReponseType("status update");
                        r2.setUsers(names);
                        r2.setStatus(arr);
                        for (ChatHandler ch2 : clients) {
                            //System.out.println(r2.getStatus());
                            if (!ch2.username.equals(mess.getUserName()) && !ch2.username.equals(mess.getDistUserName())) {
                                ch2.ps.writeObject(r2);
                            }
                        }

                        //System.out.println("invitaion sent to client 2");
                        break;
                    }
                }
            }
            break;

            case "cancel invitation": {

                //System.out.println(" I am the server I got "+ mess.getRequestType() + "request from "+ mess.getUserName() + "To "+mess.getDistUserName());
                for (ChatHandler ch : clients) {
                    if (ch.getUserName().equals(mess.getDistUserName())) {
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("cancel invitation");
                        ch.ps.writeObject(r);

                        //System.out.println("cancel invitation sent to client 2");
                        break;
                    }
                }
            }
            break;

            case "invitation response": {

                //System.out.println("invite response is recieved");
                for (ChatHandler ch : clients) {
                    if (ch.getUserName().equals(mess.getDistUserName())) {
                        r.setUserName(mess.getUserName());
                        r.setDestUsername(mess.getDistUserName());
                        r.setReponseType("invitation response");
                        r.setInvitationReply(mess.getInvitationReply());
                        //System.out.println("I am server sending "+ r.getReponseType() +  "to "+mess.getDistUserName());

                        // Cheking if player 2 accepted the invitation or not 
                        if (r.getInvitationReply()) //if yes we need to instantiate game object
                        {

                            r.setPlayer1Name(mess.getDistUserName());
                            r.setPlayer2Name(mess.getUserName());
                            r.setGameBoard(quickGameInitArr);

                            ch.ps.writeObject(r);
                            Server.updateStatus(mess.getUserName(), 2);
                            Server.updateStatus(mess.getUserName(), 2);

                        } else {
                            Server.updateStatus(mess.getUserName(), 1);
                            Server.updateStatus(mess.getDistUserName(), 1);
                            ch.ps.writeObject(r);
                        }

                        Response r2 = new Response();
                        int[] arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        String[] names = Server.myServ.users.toArray(new String[Server.myServ.users.size()]);
                        int[] sco = Server.myServ.scores.stream().mapToInt(j -> j).toArray();
                        r2.setScores(sco);
                        arr = Server.myServ.status.stream().mapToInt(j -> j).toArray();
                        r2.setReponseType("status update");
                        r2.setUsers(names);
                        r2.setStatus(arr);
                        for (ChatHandler ch2 : clients) {
                            //System.out.println(r2.getStatus());
                            if (!ch2.username.equals(mess.getUserName()) && !ch2.username.equals(mess.getDistUserName())) {
                                ch2.ps.writeObject(r2);
                            }
                        }

                        break;
                    }
                }
            }
            break;

            case "sendMsg": {
                String toUser = mess.getDistUserName();
                String fromUser = mess.getUserName();
                String message = mess.getChatMsg();
                //System.out.println("Got send message request");

                for (ChatHandler ch : clients) {
                    if (ch.getUserName().equals(mess.getDistUserName())) {
                        Response rr = new Response();
                        rr.setUserName(fromUser);
                        rr.setDestUsername(toUser);
                        rr.setReponseType("recieveMsg");
                        rr.setMessage(message);
                        ch.ps.writeObject(rr);
                    }
                }
            }
            break;

            case "moveToO": {
//                
                Response gameRes = new Response();
                gameRes.setReponseType("receiveInO");
                gameRes.setGameBoard(mess.getGameBoard());
                gameRes.setPlayer1Name(mess.getPlayer1Name());
                gameRes.setPlayer2Name(mess.getPlayer2Name());
                //System.out.println("Got the first move");
                for (ChatHandler ch : clients) {
                    // if (ch.getUserName().equals(g.getPlayer2Name()))
                    if (ch.getUserName().equals(mess.getPlayer2Name())) {
                        ch.ps.writeObject(gameRes);

                        if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "1wins") {
                            DB.increaseScore(mess.getPlayer1Name()); // Increasing score
                            Response result = new Response();
                            result.setReponseType("win");
                            this.ps.writeObject(result);

                            result.setReponseType("lose");
                            ch.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);

                        } else if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "2wins") {
                            DB.increaseScore(mess.getPlayer2Name()); // Increasing score
                            Response result = new Response();
                            result.setReponseType("win");
                            ch.ps.writeObject(result);

                            result.setReponseType("lose");
                            this.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);
                        } else if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "tie") {
                            Response result = new Response();
                            result.setReponseType("TIE");
                            ch.ps.writeObject(result);
                            this.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);
                        }

                        break;
                    }
                }
            }
            break;

            case "moveToX": {
                //g.increaseLastMovePlayer();
                //g.setGameBoard(mess.getGameBoard());
                Response gameRes = new Response();
                gameRes.setReponseType("receiveInX");
                gameRes.setPlayer1Name(mess.getPlayer1Name());
                gameRes.setPlayer2Name(mess.getPlayer2Name());
                gameRes.setGameBoard(mess.getGameBoard());
                for (ChatHandler ch : clients) {
                    if (ch.getUserName().equals(mess.getPlayer1Name())) {
                        ch.ps.writeObject(gameRes);

                        if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "1wins") {

                            DB.increaseScore(mess.getPlayer1Name()); // Increasing score
                            Response result = new Response();
                            result.setReponseType("win");
                            ch.ps.writeObject(result);

                            result.setReponseType("lose");
                            this.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);

                        } else if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "2wins") {
                            DB.increaseScore(mess.getPlayer2Name()); // Increasing score
                            Response result = new Response();
                            result.setReponseType("win");
                            this.ps.writeObject(result);

                            result.setReponseType("lose");
                            ch.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);
                        } else if (checkWinner(mess.getGameBoard(), mess.getCurrentMoveRow(), mess.getCurrentMoveCol()) == "tie") {
                            Response result = new Response();
                            result.setReponseType("TIE");
                            ch.ps.writeObject(result);
                            this.ps.writeObject(result);
                            Server.updateStatus(this.getUserName(), 1);
                            Server.updateStatus(ch.getUserName(), 1);
                        }

                        break;
                    }
                }
            }
            break;

            case "set username": {
                //System.out.println("invite request is recieved");
                this.username = r.getUserName();
            }
        }

        if (mess.getRequestType().equals("signInSubmit") || mess.getRequestType().equals("signUpSubmit") || mess.getRequestType().equals("set username")) {
            //System.out.println("I am server sending "+ r.getReponseType() +  "to "+r.getDestUsername());
            this.ps.writeObject(r);

        }

    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String s) {
        this.username = s;
    }

    public static void close() {

        Response r2 = new Response();
        r2.setReponseType("server closed");

        for (ChatHandler ch : clients) {
            try {
                ch.ps.writeObject(r2);
                ch.finish = true;
                ch.ps.close();
                ch.dis.close();
                clients.remove(ch);
                ch.s.close();
            } catch (IOException ex) {
                System.out.println("client unreachable");
            }
        }
        System.out.println("all users removed");
    }

    public String checkWinner(int[][] board, int lastMoveRow, int lastMoveCol) {                // will be called after each increase in lastmove player
        // dummy method for 3*3 boards only

        String gameState = "Nothing Yet";
        int NumofMoves = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1 || board[i][j] == 0) {
                    NumofMoves++;
                }
            }

        }

        // Check for horizontal
        if ((board[lastMoveRow][0] == board[lastMoveRow][2]) && (board[lastMoveRow][1] == board[lastMoveRow][2]) && (board[lastMoveRow][0] == board[lastMoveRow][lastMoveCol])) {
            if (board[lastMoveRow][lastMoveCol] == 1) {
                return gameState = "1wins";
            }
            if (board[lastMoveRow][lastMoveCol] == 0) {
                return gameState = "2wins";
            }

        }

        // Check for vertical 
        if ((board[0][lastMoveCol] == board[2][lastMoveCol]) && (board[1][lastMoveCol] == board[2][lastMoveCol]) && (board[0][lastMoveCol] == board[lastMoveRow][lastMoveCol])) {
            if (board[lastMoveRow][lastMoveCol] == 1) {
                return gameState = "1wins";
            }
            if (board[lastMoveRow][lastMoveCol] == 0) {
                return gameState = "2wins";
            }

        }

        // Check for diagonal (check later)
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[2][0] == board[1][1] && board[1][1] == board[0][2])) {
            if (board[1][1] == 1) {
                return "1wins";
            }
            if (board[1][1] == 0) {
                return "2wins";
            }
        }

        // Check for tie case 
        if (NumofMoves == 9 && (!gameState.equals("1wins")) && (!gameState.equals("2wins"))) {
            return gameState = "tie";
        }

        return gameState;

    }

}
