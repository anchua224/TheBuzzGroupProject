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

    public ResourceTable(Connection mConnection) throws SQLException {
        mCreateTable = mConnection.prepareStatement("CREATE TABLE resources (id INT NOT NULL, com_id INT, user_id VARCHAR(64),"+
            "res_id SERIAL PRIMARY KEY, link VARCHAR(500), FOREIGN KEY (id) REFERENCES ideas(id), " + 
            "FOREIGN KEY (com_id) REFERENCES COMMENTS(com_id), FOREIGN KEY (user_id) REFERENCES USERS(user_id))");        
        mDropTable = mConnection.prepareStatement("DROP TABLE resources");     
        mSelectAll = mConnection.prepareStatement("SELECT * FROM resources");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM resources WHERE res_");
        mDeleteOne = mConnection.prepareStatement("DELETE FROM resources WHERE id = ?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO resources VALUES (?, ?, ?, defult, ?)");
        //mUpdateOne = mConnection.prepareStatement("UPDATE resources SET subject = ?, message = ? WHERE id = ?");
    }

    //inner class Resource, holds information of one row in the Resource table
    class Resource{
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
         * @param id ideas id
         * @param com_id comments if (if have, other wise 0)
         * @param user_id user_id of the uploder
         * @param res_id PK of resourse
         * @param link
         */
        public Resource(int id, int com_id, String user_id, int res_id, String link){
            this.id = id;
            this.com_id = com_id;
            this.user_id = user_id;
            this.link = link;
        }

    }
    
}
