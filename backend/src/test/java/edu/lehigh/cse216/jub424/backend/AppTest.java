package edu.lehigh.cse216.jub424.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Map;


import edu.lehigh.cse216.jub424.backend.data_structure.*;
import edu.lehigh.cse216.jub424.backend.data_manager.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testIdeaConstructor() {
        int id = 1500;
        String title = "Test Title";
        String content = "Test Content";
        int like = 20;
        int dislike = 6;
        Idea idea = new Idea(id,title,content,like,dislike);

        assertTrue(idea.id == id);
        assertTrue(idea.title.equals(title));
        assertTrue(idea.massage.equals(content));
        assertTrue(idea.like == like);
        assertTrue(idea.dislike == dislike);
    }

    /**
     * test if it can connect to the heroku database by the DATABASE_URL
     * @throws SQLException
     */
    public void testConnection(){
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                fail("Error: DriverManager.getConnection() returned a null object");
            }
            conn.close();
        } catch (SQLException e) {
            fail("connect fail because the SQLException" + e.getMessage());
        } catch (ClassNotFoundException cnfe) {
            fail("Unable to find postgresql driver");
        } catch (URISyntaxException s) {
            fail("URI Syntax Error");
        }
    }

    /**
     * test to see if this can set all the manager ok
     * the set up of manager is in the getDatabase() function
     */
    public void testManager(){
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database mDatabase = Database.getDatabase(db_url);
        assert(mDatabase != null);
        try {
            mDatabase.mIdeaTableManager = new IdeaTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the ideas Table Manager");
        }
        mDatabase.disconnect();
    }

    /**
     * test to see if this can set all the routes ok
     * the set up of routes is in the getDatabase() function
     */
    public void testRoutes(){
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database mDatabase = Database.getDatabase(db_url);
        assert(mDatabase != null);
        try {
            DatabaseRoutes.ideasRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of ideas table");
        }
        mDatabase.disconnect();
    }
}
