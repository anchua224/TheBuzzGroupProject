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

class IdeaTable{
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

    /**
     * A prepared statement for get like count
     */
    private static PreparedStatement mGetLike;

    /**
     * A prepared statement for updating a single row add like
     */
    private static PreparedStatement mLike;

    /**
     * A prepared statement for get like count
     */
    private static PreparedStatement mGetDislike;

    /**
     * A prepared statement for updating a single row add dislike
     */
    private static PreparedStatement mDislike;

    public IdeaTable(Connection mConnection) throws SQLException {
        // CREATE TABLE ideas (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL,
        // message VARCHAR(500) NOT NULL,likecount INT NOT NULL, dislikecount INT not
        // NULL)
        mCreateTable = mConnection.prepareStatement(
            "CREATE TABLE IDEAS (id SERIAL PRIMARY KEY, subject VARCHAR(50) NOT NULL,"+
             "message VARCHAR(500) NOT NULL,likecount INT NOT NULL, dislikecount INT NOT "+
             "NULL)");
        mDropTable = mConnection.prepareStatement("DROP TABLE IDEAS");     
        mSelectAll = mConnection.prepareStatement("SELECT * FROM ideas ORDER BY id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * from ideas WHERE id=?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM ideas WHERE id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO ideas VALUES (default, ?, ?, 0, 0)");
        mUpdateOne = mConnection.prepareStatement("UPDATE ideas SET subject = ?, message = ? WHERE id = ?");
        mGetLike = mConnection.prepareStatement("SELECT likecount from ideas WHERE id=?");
        mLike = mConnection.prepareStatement(
                "UPDATE ideas SET likecount = (SELECT likecount from ideas WHERE id = ?) + 1 WHERE id = ?");
        mGetDislike = mConnection.prepareStatement("SELECT dislikecount from ideas WHERE id=?");
        mDislike = mConnection.prepareStatement(
                "UPDATE ideas SET dislikecount = (SELECT dislikecount from ideas WHERE id = ?) + 1 WHERE id = ?");
    }

    class Idea{
        /**
        * id, which is the primary key of idea
        */
        public int id;

        /**
        * The title for this idea
        */
        public String title;

        /**
        * massage in the idea which is a string
        */
        public String massage;

        /**
        * the like counter of the idea
        */
        public int like;

        /**
        * the dislike counter of the idea
        */
        public int dislike;

        public Idea(int id, String title, String massage){
            this.id = id;
            this.title = title;
            this.massage = massage;
            this.like = 0;
            this.dislike = 0;
        }
        public Idea(int id, String title, String massage,int like, int dislike){
            this.id = id;
            this.title = title;
            this.massage = massage;
            this.like = like;
            this.dislike = dislike;
        }

    }

    
    /**
    * Create the IDEAS table
    */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
    * Drop the IDEAS table
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
    public ArrayList<Idea> selectAllIdeas() {
            ArrayList<Idea> res = new ArrayList<Idea>();
            try {
                ResultSet rs = mSelectAll.executeQuery();
                while (rs.next()) {
                    res.add(new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message")
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
     * Get all idea for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The idea for the requested row, or null if the ID was invalid
     */
    public Idea selectOneIdea(int id) {
        Idea res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Idea(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("message"),
                        rs.getInt("likecount"),
                        rs.getInt("dislikecount"));
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
    public int deleteIdea(int id) {
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
    public int insertIdea(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
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
    public int updateIdea(int id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the likecount for a row in the database let it + 1
     * 
     * @param id The id of the row to update
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int likeIdea(int id) {
        int res = -1;
        try {
            mLike.setInt(1, id);
            mLike.setInt(2, id);
            res = mLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the dislikecount for a row in the database let it + 1
     * 
     * @param id The id of the row to update
     *
     */
    public int dislikeIdea(int id) {
        int res = -1;
        try {
            mDislike.setInt(1, id);
            mDislike.setInt(2, id);
            res = mDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }




    

    // /**
    //  * get the likecount of an ideas of specific id
    //  * 
    //  * @param id The id of the row to get like count
    //  * 
    //  * @return The number of like count of the row. -1 indicates an error.
    //  */
    // public int getLikeCount(int id) {
    //     int res = -1;
    //     try {
    //         mGetLike.setInt(1, id);
    //         ResultSet rs = mGetLike.executeQuery();
    //         if (rs.next()) {
    //             res = rs.getInt("likecount");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return res;
    // }
    // /**
    //  * get the likecount of an ideas of specific id
    //  * 
    //  * @param id The id of the row to get like count
    //  * 
    //  * @return The number of like count of the row. -1 indicates an error.
    //  */
    // public int getDislikeCount(int id) {
    //     int res = -1;
    //     try {
    //         mGetDislike.setInt(1, id);
    //         ResultSet rs = mGetDislike.executeQuery();
    //         if (rs.next()) {
    //             res = rs.getInt("dislikecount");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return res;
    // }

}


