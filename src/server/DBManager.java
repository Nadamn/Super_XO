/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nada
 */
public class DBManager {
    Connection con;
    DriverManager DM;
    
    public DBManager() {
        try
        {
            DM.registerDriver(new org.gjt.mm.mysql.Driver());
            con = DM.getConnection("jdbc:mysql://localhost:3306/xo","root","");
            System.out.println("Success");
            
        }
        catch(SQLException e)
        {
            System.out.println("failure");
            e.printStackTrace();
        }
    }
    
    public Vector<Player> getAllPlayers(){
        Vector<Player> players = new Vector<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ResultSet rs= ps.executeQuery();
            while (rs.next()){
                Player pl = new Player(rs.getString(2), rs.getString(3));
                pl.setScore(rs.getInt(4));
                pl.setId(rs.getInt(1));
                players.add(pl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;
    }
    
    public Boolean createNewPlayer(Player player){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO player(username, password, score) VALUES (?,?,0)"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassWord());
            //ps.setInt(3, player.getScore());
            int rs= ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Player getPlayer(int id){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE id=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setInt(1, id);
            ResultSet rs= ps.executeQuery();
            Player player = null;
            while (rs.next()){
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(id);
            }
            return player;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Player getPlayer(String username){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE username=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, username);
            ResultSet rs= ps.executeQuery();
            Player player = null;
            while (rs.next()){
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(rs.getInt(1));
            }
            return player;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    
    public Player getPlayerByName(String name){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE username=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, name);
            ResultSet rs= ps.executeQuery();
            Player player = null;
            while (rs.next()){
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(rs.getInt(1));
            }
            return player;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
////    public Vector<Game> getAllGames(){
////        Vector<Game> games = new Vector<>();
////        try {
////            PreparedStatement ps = con.prepareStatement("SELECT * FROM game"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
////            ResultSet rs= ps.executeQuery();
////            while (rs.next()){
////                String gameBoardString = rs.getString(3);
////                Integer [] gameBoard = new Integer[gameBoardString.toCharArray().length];
////                for ( int i=0; i< gameBoardString.toCharArray().length; i++){
////                    gameBoard[i] = Character.getNumericValue(gameBoardString.charAt(i));
////                }
////                Game gl = new Game( (Player) rs.getObject(1), (Player) rs.getObject(2), gameBoard );
////                games.add(gl);
////            }
////        } catch (SQLException ex) {
////            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        return games;
////    }
//    
////    public Boolean createNewGame(Game game){
////        try {
////            PreparedStatement ps = con.prepareStatement("INSERT INTO game(player1id, player2id, gamestate) VALUES (?,?,?)"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
////            String gameBoardString= "";
////            for (Integer element: game.getGameBoard()){
////                gameBoardString += element.toString();
////            }
////            ps.setInt(1, (int) game.getPlayer1().getId());
////            ps.setInt(2, (int) game.getPlayer2().getId());
////            ps.setString(3, gameBoardString);
////            int rs= ps.executeUpdate();
////            return true;
////        } catch (SQLException ex) {
////            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
////            return false;
////        }
////    }
//    
////    public Game getGame(int player1Id, int player2Id){
////        try {
////            PreparedStatement ps = con.prepareStatement("SELECT * FROM game WHERE player1id=? AND player2id=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
////            ps.setInt(1, player1Id);
////            ps.setInt(2, player2Id);
////            ResultSet rs= ps.executeQuery();
////            Game game = null;
////            while (rs.next()){
////                Player p1 = getPlayer(rs.getInt(1));
////                Player p2 = getPlayer(rs.getInt(2));
////                String gameBoardString = rs.getString(3);
////                Integer [] gameBoard = new Integer[gameBoardString.toCharArray().length];
////                for ( int i=0; i< gameBoardString.toCharArray().length; i++){
////                    gameBoard[i] = Character.getNumericValue(gameBoardString.charAt(i));
////                }
////                game = new Game(p1, p2, gameBoard);
////            }
////            return game;
////        } catch (SQLException ex) {
////            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
////            return null;
////        }
////    }
    
    public Boolean updateGameScore(Game game, Integer [] score){
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE game SET gamestate=? WHERE player1id=? AND player2id=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            String gameBoardString= "";
            for (Integer element: score){
                gameBoardString += element.toString();
            }
            ps.setString(1, gameBoardString);
            ps.setInt(2, game.getPlayer1().getId());
            ps.setInt(3, game.getPlayer2().getId());
            int rs= ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static void main(String[] args) {
        DBManager db = new DBManager();    
        
//        Player p1 = new Player("nada", "nada");
//        Player p2 = new Player("bahaa", "bahaa");
//        Player p3 = new Player("mostafa", "mostafa");        
//        db.createNewPlayer(p3);
//        db.createNewPlayer(p2);
//        db.createNewPlayer(p1);
        
        
//        Player p1 = db.getPlayer(7);
//        Player p2 = db.getPlayer(8);
//        Integer [] gameState = {0,0,0,0,0,0,0,0,0};
//        Game g = new Game(p1, p2, gameState);
//        db.createNewGame(g);
        
        //Game g= db.getGame(7, 8);
        Integer [] score = {1, 2, 3, 1, 2, 3, 5 , 5,9};
      //  db.updateGameScore(g, score);
       // System.out.println(g.getGameBoard()[1]);
       
    }
    
}
