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
import java.sql.Statement;
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
            con = DM.getConnection("jdbc:mysql://sql3.freemysqlhosting.net:3306/sql3277229","sql3277229","nr7ins6aHR");
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
                Player pl = new Player(rs.getString(4), rs.getString(5), rs.getString(2));
                players.add(pl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;
    }
    
    public Boolean createNewPlayer(Player player){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO player(name, score, username, password) VALUES (?,?,?,?)"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, player.getName());
            ps.setInt(2, player.getScore());
            ps.setString(3, player.getUsername());
            ps.setString(4, player.getPassWord());
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
            Player player = new Player( rs.getString(4), rs.getString(5), rs.getString(2));
            return player;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static void main(String[] args) {
        DBManager db = new DBManager();    
        Vector<Player> players = db.getAllPlayers();
        Player pl = new Player("mostafa", "mostafa", "mostafa");
        db.createNewPlayer(pl);
        
        System.out.println(players.get(0).getName());
    }
    
}
