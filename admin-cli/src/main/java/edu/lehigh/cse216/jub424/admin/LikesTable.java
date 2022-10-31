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

class LikesTable{
    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertLike;

    /**
     * A prepared statement for getting the like count
     */
    private static PreparedStatement mGetLike;

    /**
     * A prepared statement for deleting all likes relative to an idea
     */
    private static PreparedStatement mDeleteIdea;

    /**
     * create the LIKES table
     */
    private static PreparedStatement mCreateTable;
     
    /**
     * drop the LIKES table
     */
    private static PreparedStatement mDropTable;


    /**
     * for prepared statement
     * @param take in the connection object
     */
    public LikesTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE likes (id INT, user_id VARCHAR(64), " + 
        "FOREIGN KEY (id) REFERENCES ideas(id),  FOREIGN KEY (user_id) REFERENCES USERS(user_id), " + 
        "PRIMARY KEY(id, user_id)");
        mDropTable =mConnection.prepareStatement("DROP TABLE LIKES");
        mGetLike = mConnection.prepareStatement("SELECT count(*) from likes WHERE id=?");
        mInsertLike = mConnection.prepareStatement("INSERT INTO likes VALUES (?, ?)");
        mDeleteIdea = mConnection.prepareStatement("DELETE FROM likes WHERE id = ?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM likes WHERE id = ? AND user_id = ?)");
    }
    /**
    * Create the LIKES table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the LIKES table
    */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * insert the likecount for a row in the database let it + 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were inserted
     */
    public int likeIdea(int id) {
        int count = 0;
        try {
            mInsertLike.setInt(1, id);
            count += mInsertLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * Update the likecount for a row in the database let it - 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int cancelLikeIdea(int id) {
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
     * delete all likes relative to an idea
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int deleteLikeIdea(int id) {
        int res = -1;
        try {
            mDeleteIdea.setInt(1, id);
            res = mDeleteIdea.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * get the likecount of an idea of a specific id
     * 
     * @param id The id of the row to get like count
     * 
     * @return The number of like count of the row. -1 indicates an error.
     */
    public int getLikeCount(int id) {
        int res = -1;
        try {
            mGetLike.setInt(1, id);
            ResultSet rs = mGetLike.executeQuery();
            if (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}