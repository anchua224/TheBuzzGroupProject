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
    private static PreparedStatement mSelectAllComments;

    /**
     * A prepared statement for getting one row from the database
     */
    private static PreparedStatement mSelectOneComment;

    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOneComment;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mAddComment;

    /**
     * A prepared statement for updating a single row in the database
     */
    private static PreparedStatement mUpdateComment;

    public CommentsTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE comments (id INT, user_id VARCHAR(64), " + 
        "com_id SERIAL PRIMARY KEY, FOREIGN KEY (id) REFERENCES ideas(id), FOREIGN KEY (user_id) REFERNCES USERS(user_id), " + 
        "content VARCHAR(300))");        
        mDropTable = mConnection.prepareStatement("DROP TABLE COMMENTS");     
        mSelectAllComments = mConnection.prepareStatement("SELECT * FROM comments ORDER BY com_id DESC");
        mSelectOneComment = mConnection.prepareStatement("SELECT * FROM comments WHERE com_id = ?");
        mDeleteOneComment = mConnection.prepareStatement("DELETE FROM comments WHERE com_id = ?");
        mAddComment = mConnection.prepareStatement("INSERT INTO comments VALUES (?, ?, default, ?)");
        mUpdateComment = mConnection.prepareStatement("UPDATE comments SET content = ? WHERE com_id = ?");
    }

    //inner class Comments, holds information of one row in the Comments table
    class Comments{
        /**
        * com_id, which is the primary key of Comments
        */
        public int com_id;

        /**
        * content in the Comments which is a string
        */
        public String content;

         /**
        * Create a new Comment with the com_id, and content. 
        * 
        * @param com_id com_id to the Comments, which is unique for the whole time
        * @param content content of the Comments
        */
        public Comments(int com_id, String content){
            this.com_id = com_id;
            this.content = content;
        }
    }
    /**
    * Create the Comments table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the Comments table
    */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Query the database for a list of all subjects and their com_ids
    * 
    * @return All rows, as an ArrayList
    */
    public ArrayList<Comments> selectAllComments() {
            ArrayList<Comments> res = new ArrayList<Comments>();
            try {
                ResultSet rs = mSelectAllComments.executeQuery();
                while (rs.next()) {
                    res.add(new Comments(
                        rs.getInt("com_id"),
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
     * Get all Comments for a specific idea, by id
     * 
     * @param id The id of the row being requested
     * 
     * @return The Comments for the requested row, or null if the com_id was invalid
     */
    public Comments selectOneComments(int com_id) {
        Comments res = null;
        try {
            mSelectOneComment.setInt(1, com_id);
            ResultSet rs = mSelectOneComment.executeQuery();
            if (rs.next()) {
                res = new Comments(
                        rs.getInt("com_id"),
                        rs.getString("content")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Delete a row by com_id
     * 
     * @param com_id The com_id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    public int deleteComments(int com_id) {
        int res = -1;
        try {
            mDeleteOneComment.setInt(1, com_id);
            res = mDeleteOneComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Insert a row into the database
     * 
     * @param id The id for the idea you want to comment
     * @param comment The comment for this new row
     * @param email The email for the user commenting
     * 
     * @return The number of rows that were inserted
     */
    public int insertComments(int id, String content, String email) {
        int count = 0;
        try {
            mAddComment.setInt(1, id);
            mAddComment.setString(2, HashFunc.hash(email));
            mAddComment.setString(3, content);
            count += mAddComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * Update the subject and message for a row in the database
     * 
     * @param com_id      The com_id of the row to update
     * 
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateComments(int com_id, String content) {
        int res = -1;
        try {
            mUpdateComment.setString(1, content);
            mUpdateComment.setInt(2, com_id);
            res = mUpdateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}


