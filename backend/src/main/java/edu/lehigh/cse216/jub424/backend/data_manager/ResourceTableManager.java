package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.net.URL;
import java.sql.*;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * ResourceTableManager are all function interact with the resources table
 * 
 * @author Ala Chua
 * @version 1.0.0
 * @since 2022-11-26
 */
public class ResourceTableManager {
    /**
     * A prepared statement for inserting a row from the database
     */
    private static PreparedStatement mInsertResource;
    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteResource;
    /**
     * A prepared statement for updating a row from the database
     */
    private static PreparedStatement mUpdateResource;
    /**
     * A prepared statement for selecting a row from the database
     */
    private static PreparedStatement mSelectOneResource;
    /**
     * A prepared statement for selecting all the rows in database
     */
    private static PreparedStatement mSelectAll;

    /**
     * A prepared statement for finding a resource in database
     */
    private static PreparedStatement mFindResource;

    /**
     * ResourceTableManager manage all the SQL queries related to resources table
     * 
     * @param mConnection connection to the databse
     * @throws SQLException when there is an error related to sql
     */
    public ResourceTableManager(Connection mConnection) throws SQLException {
        mInsertResource = mConnection.prepareStatement("INSERT INTO resources VALUES (?, ?, ?, default, ?, ?)");
        mDeleteResource = mConnection.prepareStatement("DELETE FROM resources WHERE res_id=?");
        mUpdateResource = mConnection.prepareStatement("UPDATE resources SET link=? WHERE res_id=?");
        mSelectOneResource = mConnection.prepareStatement("SELECT * FROM resources WHERE (com_id=? AND idea_id=?)");
        mSelectAll = mConnection.prepareStatement("SELECT * FROM resources");
        mFindResource = mConnection.prepareStatement("SELECT count(*) where res_id=?");
    }

    /**
     * insert a new resource to table
     *
     * @param idea_id  id of the idea
     * @param com_id   id of the comment
     * @param user_id  id of the user
     * @param link     source for resource
     * @param validity validity of the resource
     * 
     * @return number of rows inserted to the table
     * @throws Exception
     */
    public int insertOneResource(int idea_id, int com_id, String user_id, String link, int validity) throws Exception {
        int count = 0;
        // Test if link is valid
        try {
            URL url = new URL(link);
            url.toURI();
        } catch (Exception e) {
            throw new Exception("Link not valid: " + link);
        }
        try {
            mInsertResource.setInt(1, idea_id);
            mInsertResource.setInt(2, com_id);
            mInsertResource.setString(3, user_id);
            mInsertResource.setString(5, link);
            mInsertResource.setInt(6, validity);
            count += mInsertResource.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * delete a resource from database
     * 
     * @param res_id The id of the resource
     * 
     * @return number of rows being deleted
     */
    public int deleteOneResource(int res_id) {
        int res = 0;
        try {
            mDeleteResource.setInt(4, res_id);
            res = mDeleteResource.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * update one row of resources table
     *
     * @param res_id id of the resource
     * @param link   new resource link
     * 
     * @return number of rows inserted to the table
     */
    public int updateResource(int res_id, String link) {
        int res = 0;
        try {
            mUpdateResource.setString(1, link);
            mUpdateResource.setInt(2, res_id);
            res = mUpdateResource.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * select one resource from database
     * 
     * @param com_id  The id of the comment
     * 
     * @param idea_id The id of the idea
     * 
     * @return number of rows being deleted
     */
    public Resource selectOneResource(int com_id, int idea_id) {
        Resource res = null;
        try {
            mSelectOneResource.setInt(1, com_id);
            mSelectOneResource.setInt(2, idea_id);
            ResultSet rs = mSelectOneResource.executeQuery();
            if (rs.next()) {
                res = new Resource(
                        rs.getInt("idea_id"),
                        rs.getInt("com_id"),
                        rs.getString("user_id"),
                        rs.getInt("res_id"),
                        rs.getString("link"),
                        rs.getInt("validity"));
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * select all the resources
     * 
     * @return An arraylist with all the resource objects
     */
    public ArrayList<Resource> selectAllResources() {
        ArrayList<Resource> res = new ArrayList<>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            if (rs.next()) {
                res.add(new Resource(
                        rs.getInt("idea_id"),
                        rs.getInt("com_id"),
                        rs.getString("user_id"),
                        rs.getInt("res_id"),
                        rs.getString("link"),
                        rs.getInt("validity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * find if a resource is in the table
     * 
     * @param res_id The id of the resource
     * 
     * @return A boolean value. True if the resource in the table
     */
    public boolean findResource(String res_id) {
        boolean flag = false;
        try {
            mFindResource.setString(4, res_id);
            ResultSet rs = mFindResource.executeQuery();
            if (rs.next()) {
                if (rs.getInt("count") != 0) {
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
