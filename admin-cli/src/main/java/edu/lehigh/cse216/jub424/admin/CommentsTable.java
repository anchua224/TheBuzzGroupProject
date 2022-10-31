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

class CommentsTable{
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

    public CommentsTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE comments (id INT, user_id VARCHAR(64), " + 
        "com_id SERIAL PRIMARY KEY, FOREIGN KEY (id) REFERENCES ideas(id), FOREIGN KEY (user_id) REFERNCES USERS(user_id), " + 
        "content VARCHAR(300))");        
        mDropTable = mConnection.prepareStatement("DROP TABLE COMMENTS");     
        mSelectAll = mConnection.prepareStatement("SELECT * FROM comments ORDER BY com_id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT FROM comments WHERE com_id = ?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM comments WHERE com_id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO comments VALUES (default, default, default, ?)");
        mUpdateOne = mConnection.prepareStatement("UPDATE comments SET content = ? WHERE com_id = ?");
    }

    //inner class Comments, holds information of one row in the Comments table
    class Comments{
        /**
        * id, which is the primary key of Comments
        */
        public int id;

        /**
        * content in the Comments which is a string
        */
        public String content;

         /**
        * Create a new Comment with the provided id, and content. And a
        * creation date based on the system clock at the time the constructor was
        * called. 
        * 
        * @param id id to the Comments, which is unique for the whole time
        * 
        * @param content content of the Comments
        */
        public Comments(int id, String content){
            this.id = id;
            this.content = content;
        }
    }
    /**
    * Create the CommentsS table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the CommentsS table
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
    public ArrayList<Comments> selectAllComments() {
            ArrayList<Comments> res = new ArrayList<Comments>();
            try {
                ResultSet rs = mSelectAll.executeQuery();
                while (rs.next()) {
                    res.add(new Comments(
                        rs.getInt("id"),
                        rs.getString("content")
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
     * Get all Comments for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The Comments for the requested row, or null if the ID was invalid
     */
    public Comments selectOneComments(int id) {
        Comments res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Comments(
                        rs.getInt("id"),
                        rs.getString("content")
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
    public int deleteComments(int id) {
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
     * 
     * @return The number of rows that were inserted
     */
    public int insertComments(String content) {
        int count = 0;
        try {
            mInsertOne.setString(1, content);
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
     * 
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateComments(int id, String content) {
        int res = -1;
        try {
            mUpdateOne.setString(1, content);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}


