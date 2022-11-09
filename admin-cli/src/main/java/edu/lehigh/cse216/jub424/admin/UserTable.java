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

class UserTable{
    private static PreparedStatement mCreateTable;

    private static PreparedStatement mDropTable;
   /**
     * A prepared statement for getting all data in the database
     */
    private static PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private static PreparedStatement mSelectUser;

    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteUser;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertUser;

    /**
     * A prepared statement for updating a single row in the database
     */
    private static PreparedStatement mUpdateUser;
    /**
     * A prepated statement for updatting the validity of the user
     */
    private static PreparedStatement mValidateUser;

    public UserTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement(
            "CREATE TABLE Users (user_id VARCHAR(64) NOT NULL, email VARCHAR(50) NOT NULL, "+
            "name VARCHAR(50) NOT NULL, GI VARCHAR(30) NOT NULL, SO VARCHAR(30) NOT NULL, " + 
            "note VARCHAR(500) NOT NULL, validity INT, PRIMARY KEY (user_id))"
        );        
        mDropTable = mConnection.prepareStatement("DROP TABLE Users");     
        mSelectAll = mConnection.prepareStatement("SELECT * FROM Users ORDER BY name DESC");
        mSelectUser = mConnection.prepareStatement("SELECT * from Users WHERE email=?");
        mDeleteUser = mConnection.prepareStatement("DELETE FROM Users WHERE email = ?");
        mInsertUser = mConnection.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?)");
        mUpdateUser = mConnection.prepareStatement("UPDATE Users SET name = ?, GI = ?, SO = ?, note = ? WHERE email = ?");
        mValidateUser = mConnection.prepareStatement("UPDATE Users SET valid = ? WHERE email = ?");
    }

    //inner class User, holds information of one row in the User table
    class User{
        /**
        * The user_id for the User
        */
        public String user_id;
        /**
        * The email for this User
        */
        public String email;
        /**
        * The name for this User
        */
        public String name;
        /**
         * The Gender Identity for this User
         */
        public String GI;
        /**
         * The Sexual Orientation for this User
         */
        public String SO;
        /**
         * The note for this User
         */
        public String note;
        /**
         *  The validity of the user
         */
        public int valid;
         /**
        * Create a new User with the provided user_id, email, name, GI, SO, note. And a
        * creation date based on the system clock at the time the constructor was
        * called. 
        * 
        * @param user_id user_id for the user
        * @param email email for the user
        * @param name name for the user
        * @param GI gender identity for the user
        * @param SO sexual orientation for the user
        * @param note note for the user
        * 
        */
        public User(String email, String name, String GI, String SO, String note){
            this.user_id = HashFunc.hash(email);
            this.email = email;
            this.name = name;
            this.GI = GI;
            this.SO = SO;
            this.note = note;
            valid = 1;
        }
    }
    /**
    * Create the UserS table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the UserS table
    */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Query the database for a list of all user info
    * 
    * @return All rows, as an ArrayList
    */
    public ArrayList<User> selectAllUsers() {
            ArrayList<User> res = new ArrayList<User>();
            try {
                ResultSet rs = mSelectAll.executeQuery();
                while (rs.next()) {
                    res.add(new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("GI"),
                        rs.getString("SO"),
                        rs.getString("note")
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
     * Get all User info for a specific row, by email
     * 
     * @param email The email of the row being requested
     * 
     * @return The User for the requested row, or null if the email was invalid
     */
    public User selectOneUser(String email) {
        User res = null;
        try {
            mSelectUser.setString(1, email);
            ResultSet rs = mSelectUser.executeQuery();
            if (rs.next()) {
                res = new User(
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("GI"),
                    rs.getString("SO"),
                    rs.getString("note")
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Delete a row by email
     * 
     * @param email The email of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    public int deleteUser(String email) {
        int res = -1;
        try {
            mDeleteUser.setString(1, email);
            res = mDeleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Insert a row into the database
     * 
     * @param email The email for this new row
     * @param name The name body for this new row
     * @param GI The gender identity for this new row
     * @param SO The new Sexual Orientation for this new row
     * @param note THe new note for this row
     * 
     * @return The number of rows that were inserted
     */
    public int insertUser(String email, String name, String GI, String SO, String note) {
        int count = 0;
        try {
            mInsertUser.setString(1, HashFunc.hash(email));
            mInsertUser.setString(2, email);
            mInsertUser.setString(3, name);
            mInsertUser.setString(4, GI);
            mInsertUser.setString(5, SO);
            mInsertUser.setString(6, note);
            count += mInsertUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * Update the user info for a row in the database
     * 
     * @param email The email of the row to update
     * @param GI The gender identity for this new row to update
     * @param SO The sexual orientation for this new row to update
     * @param note The new note for this new row tp ipdate
     * @param name The new name contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateUser(String email,String name, String GI, String SO, String note) {
        int res = -1;
        try {
            mUpdateUser.setString(1, name);
            mUpdateUser.setString(2, GI);
            mUpdateUser.setString(3, SO);
            mUpdateUser.setString(4, note);
            mUpdateUser.setString(5, email);
            res = mUpdateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int invalidateUser(String email, int valid){
        int res = -1;
        try{
            mValidateUser.setString(1, email);
            mValidateUser.setInt(2, valid);
            res = mValidateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}


