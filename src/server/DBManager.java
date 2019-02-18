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

/**
 *
 * @author Nada
 */
public class DBManager {

    Connection con;
    DriverManager DM;

    public DBManager() {
        try {
            DM.registerDriver(new org.gjt.mm.mysql.Driver());
            con = DM.getConnection("jdbc:mysql://localhost:3306/xo", "root", "");
            System.out.println("Connected to db successfully");

        } catch (SQLException e) {
            System.out.println("couldn't connect to db");
        }
    }

    public Vector<Player> getAllPlayers() {
        Vector<Player> players = new Vector<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Player pl = new Player(rs.getString(2), rs.getString(3));
                pl.setScore(rs.getInt(4));
                pl.setId(rs.getInt(1));
                players.add(pl);
            }
        } catch (SQLException ex) {
            System.out.println("couldn't execute query to get all players");
            return null;
        }
        return players;
    }

    public Boolean createNewPlayer(Player player) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO player(username, password, score) VALUES (?,?,0)"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassWord());
            int rs = ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("couldn't execute query, creating new player failed");
            return false;
        }
    }

    public boolean increaseScore(String userName) {

        try {
            PreparedStatement ps = con.prepareStatement("UPDATE player SET score = score + 1 WHERE username = ?");
            ps.setString(1, userName);
            ps.executeUpdate();

            System.out.println("Score increased");
            return true;

        } catch (SQLException ex) {
            System.out.println("couldn't execute query, increasing score failed");
            return false;
        }

    }

    public Player getPlayer(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE id=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Player player = null;
            while (rs.next()) {
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(id);
            }
            return player;
        } catch (SQLException ex) {
            System.out.println("couldn't execute query, get player failed");
            return null;
        }
    }

    public Player getPlayer(String username) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE username=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            Player player = null;
            while (rs.next()) {
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(rs.getInt(1));
            }
            return player;
        } catch (SQLException ex) {
            System.out.println("couldn't execute query, get player failed");
            return null;
        }
    }

    public Player getPlayerByName(String name) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM player WHERE username=?"); // rememer to implement (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) if there will be time
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            Player player = null;
            while (rs.next()) {
                player = new Player(rs.getString(2), rs.getString(3));
                player.setScore(rs.getInt(4));
                player.setId(rs.getInt(1));
            }
            return player;
        } catch (SQLException ex) {
            System.out.println("couldn't execute query, get player failed");
            return null;
        }
    }

}
