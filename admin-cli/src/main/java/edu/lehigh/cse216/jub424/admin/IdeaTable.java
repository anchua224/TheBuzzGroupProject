package edu.lehigh.cse216.jub424.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

/**
 * IdeaTable All functions related to interact with idea table
 * @author Na Chen
 * @version 1.0.0
 */
 public class IdeaTable{
    private static PreparedStatement mCreateTable;

    private static PreparedStatement mDropTable;
   /**
     * A prepared statement for getting all data in the database
     */
    private static PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private static PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private static PreparedStatement mUpdateOne;
    /**
     * 
     * A prepared statement for invalidating a single idea in the database
     */
    private static PreparedStatement mValidateOne;

    public IdeaTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE ideas (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL,"+
            "message VARCHAR(500) NOT NULL, validity INT NOT NULL, user_id VARCHAR(64), " + 
            "FOREIGN KEY (user_id) REFERENCES USERS(user_id))");        
        mDropTable = mConnection.prepareStatement("DROP TABLE IDEAS");     
        mSelectAll = mConnection.prepareStatement("SELECT * FROM ideas ORDER BY id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM ideas WHERE id=?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM ideas WHERE id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO ideas VALUES (default, ?, ?, 1, ?)");
        mUpdateOne = mConnection.prepareStatement("UPDATE ideas SET subject = ?, message = ? WHERE id = ?");
        mValidateOne = mConnection.prepareStatement("UPDATE ideas SET validity = ? WHERE id = ?");
    }

    //inner class Idea, holds information of one row in the idea table
    class Idea{
        /**
        * id, which is the primary key of idea
        */
        public int id;

        /**
        * The title for this idea
        */
        public String title;

        /**
        * massage in the idea which is a string
        */
        public String massage;

        /**
         * 
         * idea validility (1 = valid, 0 = invalid)
         */
        public int validity;
        /**
        * Create a new Idea with the provided id, title and massage. And a
        * creation date based on the system clock at the time the constructor was
        * called. 
        * 
        * @param id id to the idea, which is unique for the whole time
        * 
        * @title The title of this idea
        * 
        * @param massage massage of the idea
        */
        public Idea(int id, String title, String massage){
            this.id = id;
            this.title = title;
            this.massage = massage;
            validity = 1;
        }

    }
    /**
    * Create the IDEAS table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the IDEAS table
    */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Query the database for a list of all subjects and their IDs
    * 
    * @return All rows, as an ArrayList
    */
    public ArrayList<Idea> selectAllIdeas() {
            ArrayList<Idea> res = new ArrayList<Idea>();
            try {
                ResultSet rs = mSelectAll.executeQuery();
                while (rs.next()) {
                    res.add(new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message")
                        ));
                }
                rs.close();
                return res;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }    
    }    
    /**
     * Get all idea for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The idea for the requested row, or null if the ID was invalid
     */
    public Idea selectOneIdea(int id) {
        Idea res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message")
                        );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    public int deleteIdea(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * @param valid The validity of the idea
     * 
     * @return The number of rows that were inserted
     */
    public int insertIdea(String subject, String message, String email) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            mInsertOne.setString(3, HashFunc.hash(email));
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * Update the subject and message for a row in the database
     * 
     * @param id      The id of the row to update
     * @param subject The new title of the idea
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateIdea(int id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public int invalidateIdea(int id, int valid){
        int res = -1;
        try{
            mValidateOne.setInt(1, id);
            mValidateOne.setInt(2, valid);
            res = mValidateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}