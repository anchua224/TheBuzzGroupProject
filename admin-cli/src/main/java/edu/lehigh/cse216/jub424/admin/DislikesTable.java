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

class DislikesTable {
    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mRemoveDislike;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mAddDislike;

    /**
     * A prepared statement for get dislike count
     */
    private static PreparedStatement mGetDislikes;

    /**
     * A prepared statement for delete all dislike relative to an idea
     */
    private static PreparedStatement mDeleteDislikes;

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
     * 
     * @param take in the connection object
     */
    public DislikesTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE dislikes (id INT, user_id VARCHAR(64), " +
                "FOREIGN KEY (id) REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES USERS(user_id), " +
                "PRIMARY KEY(id, user_id))");
        mDropTable = mConnection.prepareStatement("DROP TABLE DISLIKES");
        mGetDislikes = mConnection.prepareStatement("SELECT count(*) from dislikes WHERE id=?");
        mAddDislike = mConnection.prepareStatement("INSERT INTO dislikes VALUES (?, ?)");
        mDeleteDislikes = mConnection.prepareStatement("DELETE FROM dislikes WHERE id=?");
        mRemoveDislike = mConnection.prepareStatement("DELETE FROM dislikes WHERE id = ? AND user_id = ?");
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
    public int DislikeIdea(int id, String email) {
        int count = 0;
        try {
            mAddDislike.setInt(1, id);
            mAddDislike.setString(2, HashFunc.hash(email));
            count += mAddDislike.executeUpdate();
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
    public int removeDislike(int id, String email) {
        int res = -1;
        try {
            mRemoveDislike.setInt(1, id);
            mRemoveDislike.setString(2, HashFunc.hash(email));
            res = mRemoveDislike.executeUpdate();
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
    public int deleteAllDislikes(int id) {
        int res = -1;
        try {
            mDeleteDislikes.setInt(1, id);
            res = mDeleteDislikes.executeUpdate();
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
            mGetDislikes.setInt(1, id);
            ResultSet rs = mGetDislikes.executeQuery();
            if (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}