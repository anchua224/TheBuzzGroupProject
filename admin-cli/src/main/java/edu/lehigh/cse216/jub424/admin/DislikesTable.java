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

class DislikesTable{
    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertDislike;

    /**
     * A prepared statement for get dislike count
     */
    private static PreparedStatement mGetDislike;

    /**
     * A prepared statement for delete all dislike relative to an idea
     */
    private static PreparedStatement mDeleteIdea;

    /**
     * create the DISLIKES table
     */
    private static PreparedStatement mCreateTable;
     
    /**
     * drop the DISLIKES table
     */
    private static PreparedStatement mDropTable;


    /**
     * for prepared statement
     * @param take in the connection object
     */
    public DislikesTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE Dislikes (id INT, user_id VARCHAR(64), " + 
        "FOREIGN KEY (id) REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES USERS(user_id), " + 
        "PRIMARY KEY(id, user_id)");
        mDropTable =mConnection.prepareStatement("DROP TABLE DISLIKES");
        mGetDislike = mConnection.prepareStatement("SELECT count(*) from dislikes WHERE id=?");
        mInsertDislike = mConnection.prepareStatement("INSERT INTO dislikes VALUES (?, default)");
        mDeleteIdea = mConnection.prepareStatement("DELETE FROM dislikes WHERE id = ?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM dislikes WHERE dislike_id IN " + 
        "(SELECT dislike_id FROM dislikes WHERE id = ? LIMIT 1)");
    }
    /**
    * Create the DISLIKES table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the DISLIKES table
    */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * insert the dislikecount for a row in the database let it + 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were inserted
     */
    public int DislikeIdea(int id) {
        int count = 0;
        try {
            mInsertDislike.setInt(1, id);
            count += mInsertDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * Update the dislikecount for a row in the database let it - 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int cancelDislikeIdea(int id) {
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
     * delete all dislikes relative to an idea
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int deleteDislikeIdea(int id) {
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
     * get the dislikecount of an ideas of specific id
     * 
     * @param id The id of the row to get like count
     * 
     * @return The number of like count of the row. -1 indicates an error.
     */
    public int getDislikeCount(int id) {
        int res = -1;
        try {
            mGetDislike.setInt(1, id);
            ResultSet rs = mGetDislike.executeQuery();
            if (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}