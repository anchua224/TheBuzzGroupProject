package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.*;

/**
 * LikeTableManager are all function interact with the dislikes table
 * @author Junchen Bao, Na Chen
 * @version 1.0.0
 * @since 2022-10-28
 */
public class DislikeTableManager {

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
     * A prepared statement for check if a user is already disliked
     */
    private static PreparedStatement mCheckDislike;


    //private static PreparedStatement mCreateTable;
    //private static PreparedStatement mDropTable;

    /**
     * This constructer set up all the sql query for the dislikes table use the
     * mConnection
     * @param mConnection the connection to the database
     * @throws SQLException when things goes worng in sql
     */
    public DislikeTableManager(Connection mConnection) throws SQLException {
        // CREATE TABLE likes (id INT, user_id VARCHAR(16), FOREIGN KEY (id) 
        // REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES user(user_id), 
        // PRIMARY KEY(id, user_id))
        // mCreateTable = mConnection.prepareStatement("CREATE TABLE dislikes (id INT, user_id VARCHAR(64), FOREIGN KEY (id) "+
        // "REFERENCES ideas(id), FOREIGN KEY (user_id) REFERENCES users(user_id),"+
        // "PRIMARY KEY(id, user_id))");
        // mDropTable = mConnection.prepareStatement("DROP TABLE dislikes");

        mGetDislike = mConnection.prepareStatement("SELECT count(*) from dislikes WHERE id=?");
        mInsertDislike = mConnection.prepareStatement("INSERT INTO dislikes VALUES (?, ?)");
        mDeleteIdea = mConnection.prepareStatement("DELETE FROM dislikes WHERE id = ?");
        mDeleteOne = mConnection.prepareStatement(
                "DELETE FROM dislikes WHERE id = ? AND user_id = ?");
        mCheckDislike = mConnection.prepareStatement("SELECT FROM dislikes WHERE id = ? AND user_id = ?");
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
     * @param id The id of the row to get dislike count
     * 
     * @return The number of dislike count of the row. -1 indicates an error.
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

    /**
     * insert the dislikecount for a row in the database let it + 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were inserted
     */
    public int dislikeIdea(int id, String user_id) {
        int count = 0;
        try {
            mInsertDislike.setInt(1, id);
            mInsertDislike.setString(2, user_id);
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
    public int cancelDislikeIdea(int id, String user_id) {
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
     * delete all dislike relative to an idea
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
     * Check if a certain user has already disliked a certain idea
     * @param id 
     * @param user_id
     * @return A boolean value. true indicates the user has disliked the idea
     */
    public boolean checkDislikeIdea(int id, String user_id){
        try {
            mCheckDislike.setInt(1, id);
            mCheckDislike.setString(2, user_id);
            ResultSet rs = mCheckDislike.executeQuery();
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
