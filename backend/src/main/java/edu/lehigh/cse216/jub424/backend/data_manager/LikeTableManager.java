package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.*;

/**
 * LikeTableManager are all function interact with the likes table
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class LikeTableManager {

    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertLike;

    /**
     * A prepared statement for get like count
     */
    private static PreparedStatement mGetLike;

    /**
     * A prepared statement for delete all like relative to an idea
     */
    private static PreparedStatement mDeleteIdea;

    /**
     * This constructer set up all the sql query for the likes table use the
     * mConnection
     * @param mConnection the connection to the database
     * @throws SQLException when things goes worng in sql
     */
    public LikeTableManager(Connection mConnection) throws SQLException {
        // CREATE TABLE likes (like_id SERIAL PRIMARY KEY, id INT, FOREIGN KEY (id)
        // REFERENCES ideas(id))

        mGetLike = mConnection.prepareStatement("SELECT count(*) from likes WHERE id=?");
        mInsertLike = mConnection.prepareStatement("INSERT INTO likes VALUES (default, ?)");
        mDeleteIdea = mConnection.prepareStatement("DELETE FROM likes WHERE id = ?");
        mDeleteOne = mConnection.prepareStatement(
                "DELETE FROM likes WHERE like_id IN (SELECT like_id FROM likes WHERE id = ? LIMIT 1)");
    }

    /**
     * get the likecount of an ideas of specific id
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
     * delete all like relative to an idea
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

}
