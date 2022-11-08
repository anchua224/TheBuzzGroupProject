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
    private static PreparedStatement mRemoveLike;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mAddLike;

    /**
     * A prepared statement for getting the like count
     */
    private static PreparedStatement mGetLikes;

    /**
     * A prepared statement for deleting all likes relative to an idea
     */
    private static PreparedStatement mDeleteLikes;

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
        "FOREIGN KEY (id) REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES USERS(user_id), " + 
        "PRIMARY KEY(id, user_id))");
        mDropTable =mConnection.prepareStatement("DROP TABLE LIKES");
        mGetLikes = mConnection.prepareStatement("SELECT count(*) from likes WHERE id=?");
        mAddLike = mConnection.prepareStatement("INSERT INTO likes VALUES (?, ?)");
        mDeleteLikes = mConnection.prepareStatement("DELETE FROM likes WHERE id=?");
        mRemoveLike = mConnection.prepareStatement("DELETE FROM likes WHERE id = ? AND user_id = ?");
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
    public int likeIdea(int id, String email) {
        int count = 0;
        try {
            mAddLike.setInt(1, id);
            mAddLike.setString(2,HashFunc.hash(email));
            count += mAddLike.executeUpdate();
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
    public int removeLike(int id, String email) {
        int res = -1;
        try {
            mRemoveLike.setInt(1, id);
            mRemoveLike.setString(2, HashFunc.hash(email));
            res = mRemoveLike.executeUpdate();
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
    public int deleteAllLikes(int id) {
        int res = -1;
        try {
           mDeleteLikes.setInt(1, id);
            res =mDeleteLikes.executeUpdate();
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
            mGetLikes.setInt(1, id);
            ResultSet rs = mGetLikes.executeQuery();
            if (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}