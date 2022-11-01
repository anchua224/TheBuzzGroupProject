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
     * A prepared statement for check if a user has already liked the idea
     */
    private static PreparedStatement mCheckLike;

    //private static PreparedStatement mCreateTable;
    //private static PreparedStatement mDropTable;

    /**
     * This constructer set up all the sql query for the likes table use the
     * mConnection
     * @param mConnection the connection to the database
     * @throws SQLException when things goes worng in sql
     */
    public LikeTableManager(Connection mConnection) throws SQLException {
        // CREATE TABLE likes (id INT, user_id VARCHAR(16), FOREIGN KEY (id) 
        // REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES user(user_id), 
        // PRIMARY KEY(id, user_id))
        // mCreateTable = mConnection.prepareStatement("CREATE TABLE likes (id INT, user_id VARCHAR(64), FOREIGN KEY (id) "+
        // "REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES users(user_id),"+
        // "PRIMARY KEY(id, user_id))");
        // mDropTable = mConnection.prepareStatement("DROP TABLE likes");

        mGetLike = mConnection.prepareStatement("SELECT count(*) from likes WHERE id=?");
        mInsertLike = mConnection.prepareStatement("INSERT INTO likes VALUES (?, ?)");
        mDeleteIdea = mConnection.prepareStatement("DELETE FROM likes WHERE id = ?");
        mDeleteOne = mConnection.prepareStatement(
                "DELETE FROM likes WHERE id = ? AND user_id = ?");
        mCheckLike = mConnection.prepareStatement("SELECT FROM likes WHERE id = ? AND user_id = ?");
    }

    // public void createTable(){
    //     try {
    //         mCreateTable.execute();
    //     } catch (SQLException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // } public void dropTable(){
    //     try {
    //         mDropTable.executeQuery();
    //     } catch (SQLException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }



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
     * @param user_id id of the user
     *
     * @return The number of rows that were inserted
     */
    public int likeIdea(int id, String user_id) {
        int count = 0;
        try {
            mInsertLike.setInt(1, id);
            mInsertLike.setString(2, user_id);
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
     * @param user_id id of the user
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int cancelLikeIdea(int id, String user_id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            mDeleteOne.setString(2, user_id);
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

    /**
     * check if a user has already liked an idea
     * @param id id of the idea
     * @param user_id id of the user
     * @return A boolean value. true indicates the user has liked the idea
     */
    public boolean checkLikeIdea(int id, String user_id){
        try {
            mCheckLike.setInt(1, id);
            mCheckLike.setString(2, user_id);
            ResultSet rs = mCheckLike.executeQuery();
            if (!rs.next() ) {
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
