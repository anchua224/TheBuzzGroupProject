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

public class ResourceTable {

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
     * A prepared statement for update into the database
     */
    private static PreparedStatement mUpdateOne;

    public ResourceTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection
                .prepareStatement("CREATE TABLE resources (id INT NOT NULL, com_id INT, user_id VARCHAR(64)," +
                        "res_id SERIAL PRIMARY KEY, link VARCHAR(500), FOREIGN KEY (id) REFERENCES ideas(id), " +
                        "FOREIGN KEY (com_id) REFERENCES COMMENTS(com_id), FOREIGN KEY (user_id) REFERENCES USERS(user_id))");
        mDropTable = mConnection.prepareStatement("DROP TABLE resources");
        mSelectAll = mConnection.prepareStatement("SELECT * FROM resources");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM resources WHERE res_id = ?");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM resources WHERE res_id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO resources VALUES (?, ?, ?, default, ?)");
        mUpdateOne = mConnection.prepareStatement("UPDATE resources SET link = ? WHERE res_id = ?");
    }

    // inner class Resource, holds information of one row in the Resource table
    class Resource {
        /**
         * id of idea
         */
        public int id;

        /**
         * com_id of comment if it have
         */
        public int com_id;

        /**
         * user_id of the people upload
         */
        public String user_id;

        /**
         * res_id, which is the primary key of resource
         */
        public int res_id;

        /**
         * String of the link in google drive
         */
        public String link;

        /**
         * create a new resource
         * 
         * @param id      ideas id
         * @param com_id  comments if (if have, other wise 0)
         * @param user_id user_id of the uploder
         * @param res_id  PK of resourse
         * @param link
         */
        public Resource(int id, int com_id, String user_id, int res_id, String link) {
            this.id = id;
            this.com_id = com_id;
            this.user_id = user_id;
            this.res_id = res_id;
            this.link = link;
        }
    }

    /**
     * Create the IDEAS table
     */
    public void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drop the IDEAS table
     */
    public void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query the database for a list of all resource in the table
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<Resource> selectAllResource() {
        ArrayList<Resource> res = new ArrayList<Resource>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new Resource(
                        rs.getInt("id"),
                        rs.getInt("com_id"),
                        rs.getString("user_id"),
                        rs.getInt("res_id"),
                        rs.getString("link")));
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
    public Resource selectOneResource(int id) {
        Resource res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Resource(
                        rs.getInt("id"),
                        rs.getInt("com_id"),
                        rs.getString("user_id"),
                        rs.getInt("res_id"),
                        rs.getString("link"));
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
    public int deleteResource(int id) {
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
     * @param id     id of idea
     * @param com_id comment id if have
     * @param email  email of user
     * @param link   link of resource
     * @return
     */
    public int insertResource(int id, int com_id, String email, String link) {
        int count = 0;
        try {
            mInsertOne.setInt(1, id);
            if (com_id != 0) {
                mInsertOne.setInt(2, com_id);
            } else {
                mInsertOne.setNull(2, Types.INTEGER);
            }
            mInsertOne.setString(3, HashFunc.hash(email));
            mInsertOne.setString(4, link);
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
     * @param subject The new title of the idea
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int updateResource(int res_id, String link) {
        int res = -1;
        try {
            mUpdateOne.setString(1, link);
            mUpdateOne.setInt(2, res_id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
