package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.*;

import edu.lehigh.cse216.jub424.backend.data_structure.*;


public class UsersTableManager {
    private static PreparedStatement mInsertUser;
    private static PreparedStatement mDeleteUser;
    private static PreparedStatement mUpdateNote;
    private static PreparedStatement mSelectOneUser;
    private static PreparedStatement mSelectPartOfUser;
    private static PreparedStatement mSelectAll;

    public UsersTableManager(Connection mConnection) throws SQLException{
        mInsertUser = mConnection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)");
        mDeleteUser = mConnection.prepareStatement("DELETE FROM users WHERE user_id=?");
        mUpdateNote = mConnection.prepareStatement("UPDATE users SET note=?");
        mSelectOneUser = mConnection.prepareStatement("SELECT * FROM users where user_id=?");
        mSelectPartOfUser = mConnection.prepareStatement("SELECT name, note FROM users where user_id=?");
        mSelectAll = mConnection.prepareStatement("SELECT * FROM users");
    }
    public int insertOneUser(String user_id, String email, String name, String GI, String SO, String note){
        int res = 0;
        try{
            mInsertUser.setString(1,user_id);
            mInsertUser.setString(2,email);
            mInsertUser.setString(3,name);
            mInsertUser.setString(4,GI);
            mInsertUser.setString(5,SO);
            mInsertUser.setString(6,note);
            res = mInsertUser.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    public int deleteOneUser(String user_id){
        int res = 0;
        try{
            mDeleteUser.setString(1,user_id);
            res = mDeleteUser.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    public int updateNote(String user_id){
        int res = 0;
        try{
            mUpdateNote.setString(1, user_id);
            res = mUpdateNote.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    public User selectOneUser(String user_id){
        User res = null;
        try{
            mSelectOneUser.setString(1,user_id);
            ResultSet rs = mSelectOneUser.executeQuery();
            if(rs.next()){
                res = new User(
                    rs.getString("user_id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("GI"),
                    rs.getString("SO"),
                    rs.getString("note")
                );
            }
            return res;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    } 

    public ArrayList<User> selectAllUsers(){
        ArrayList<User> res = new ArrayList<>();
        try{
            ResultSet rs = mSelectAll.executeQuery();
            if(rs.next()){
                res.add (new User(
                    rs.getString("user_id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("GI"),
                    rs.getString("SO"),
                    rs.getString("note")
                ));
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return res;
    }

    //not sure it should be like this and not sure if this is needed
    public User mSelectPartOfUser(String user_id){
        User res = null;
        try{
            mSelectPartOfUser.setString(1,user_id);
            ResultSet rs = mSelectPartOfUser.executeQuery();
            if(rs.next()){
                res = new User(
                    null,
                    null,
                    rs.getString("name"),
                    null,
                    null,
                    rs.getString("note")
                );
            }
            return res;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
