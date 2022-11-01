package edu.lehigh.cse216.jub424.backend.data_manager;

import java.util.ArrayList;
import java.sql.*;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * CommentsTableManager are all function interact with the comments table
 * @author Na Chen
 * @version 1.0.0
 * @since 2022-10-28
 */
public class CommentsTableManager {
    /**
     * A prepared statement for selecting all row from database
     */
    private static PreparedStatement mSelectAll;
    /**
     * A prepared statement for selecting one row from database
     */
    private static PreparedStatement mSelectOne;
    /**
     * A prepared statement for updating a row in the database
     */
    private static PreparedStatement mUpdateOne;
    /**
     * A prepared statement for inserting a row to the database
     */
    private static PreparedStatement mInsertOne;
    /**
     * A prepared statement for selecting all row related to one idea_id in database
     */
    private static PreparedStatement mSelectAllUnderOneIDea;

    //private static PreparedStatement mCreateTable;
    //private static PreparedStatement mDropTable;

    /**
     * This constructer set up all the sql query for the comments table use the
     * mConnection
     * @param mConnection the connection to the database
     * @throws SQLException when things goes worng in sql
     */
    public CommentsTableManager(Connection mConnection) throws SQLException{
        //mCreateTable = mConnection.prepareStatement("CREATE TABLE comments (id INT, user_id VARCHAR(64), com_id SERIAL PRIMARY KEY, content VARCHAR(300), FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (id) REFERENCES ideas(id))");
        //mDropTable = mConnection.prepareStatement("DROP TABLE comments");

        mSelectAll = mConnection.prepareStatement("SELECT * FROM comments ORDER BY com_id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM comments where com_id=?");
        mUpdateOne = mConnection.prepareStatement("UPDATE comments SET content=? where com_id=?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO comments VALUES (?, ?, default, ?)");
        mSelectAllUnderOneIDea = mConnection.prepareStatement("SELECT * FROM comments where id=? ORDER BY com_id DESC");
    }


    // public void createTable(){
    //     try {
    //         mCreateTable.executeQuery();
    //     } catch (SQLException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }

    // public void dropTable(){
    //     try {
    //         mDropTable.executeQuery();
    //     } catch (SQLException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }

    /**
     * get all the comments in the comments table
     * 
     * @return An arraylist contains objects of all comments in table
     */
    public ArrayList<Comment> selectAllComments(){
        ArrayList<Comment> res = new ArrayList<>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new Comment(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getInt("com_id"),
                        rs.getString("content")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 

    /**
     * get a specific comment
     * 
     * @param com_id The comment id of the row to get one comment
     * 
     * @return The comment object of the selected comment
     */
    public Comment selectOneComment(int com_id){
        Comment res = null;
        try {
            mSelectOne.setInt(1, com_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Comment(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getInt("com_id"),
                        rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * update a specific comment
     * 
     * @param com_id The comment id of the row need to be updated
     * @param content the new content of the comment
     * 
     * @return The number of like count of the row. -1 indicates an error.
     */
    public int updateOneComment(int com_id, String content){
        int res = -1;
        try {
            mUpdateOne.setString(1, content);
            mUpdateOne.setInt(2, com_id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * insert a new comment to table
     * 
     * @param id The id of the related idea
     * @param user_id the user id of the related user
     * @param content the content of the comment
     * 
     * @return The number of rows inserted to the comment table.
     */
    public int insertOneComment(int id, String user_id, String content){
        int res = 0;
        try {
            mInsertOne.setInt(1, id);
            mInsertOne.setString(2, user_id);
            mInsertOne.setString(3, content);
            res += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * select all the comments under a certain idea
     * 
     * @param id The id of the idea
     * 
     * @return An arraylist contains all the comments object under one idea
     */
    public ArrayList<Comment> selectAllComUnderOneIdea(int id){
        ArrayList<Comment> res = new ArrayList<>();
        try{
            mSelectAllUnderOneIDea.setInt(1,id);
            ResultSet rs = mSelectAllUnderOneIDea.executeQuery();
            while(rs.next()){
                res.add(new Comment(
                    rs.getInt("id"),
                    rs.getString("user_id"),
                    rs.getInt("com_id"),
                    rs.getString("content"))
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
    
}
