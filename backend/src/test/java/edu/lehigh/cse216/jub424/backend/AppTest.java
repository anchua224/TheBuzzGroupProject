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
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testIdeaConstructor() {
        int id = 1500;
        String title = "Test Title";
        String content = "Test Content";
        String user_id = "qwertyuiopasdfghjklzxcvbnm1234567890opuytrewqasdfghjklzxcvbnm34";
        Idea idea = new Idea(id, title, content,1,user_id);

        assertTrue(idea.id == id);
        assertTrue(idea.title.equals(title));
        assertTrue(idea.massage.equals(content));
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testUserConstructor() {
        String user_id = "qwertyuiopasdfghjklzxcvbnm1234567890opuytrewqasdfghjklzxcvbnm34";
        String email = "xxx000@lehigh.edu";
        String name = "First Last";
        String GI = "Female";
        String SO = "Straight";
        String note = "test note";
        User user = new User(user_id, email, name, GI, SO, note);

        assertTrue(user.user_id.equals(user_id));
        assertTrue(user.email.equals(email));
        assertTrue(user.name.equals(name));
        assertTrue(user.GI.equals(GI));
        assertTrue(user.SO.equals(SO));
        assertTrue(user.note.equals(note));
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testCommentConstructor() {
        String user_id = "qwertyuiopasdfghjklzxcvbnm1234567890opuytrewqasdfghjklzxcvbnm34";
        int id = 1500;
        int com_id = 2000;
        String content = "test content";
        Comment comment = new Comment(id, user_id, com_id, content);

        assertTrue(comment.user_id.equals(user_id));
        assertTrue(comment.content.equals(content));
        assertTrue(comment.idea_id == id);
        assertTrue(comment.com_id == com_id);
    }


    /**
     * test if it can connect to the heroku database by the DATABASE_URL
     * 
     * @throws SQLException
     */
    public void testConnection() {
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
    public void testManager() {
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database mDatabase = Database.getDatabase(db_url);
        assert (mDatabase != null);
        try {
            mDatabase.mIdeaTableManager = new IdeaTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the ideas Table Manager");
        }
        try {
            mDatabase.mLikeTableManager = new LikeTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the likes Table Manager");
        }
        try {
            mDatabase.mDislikeTableManager = new DislikeTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the dislikes Table Manager");
        }
        try {
            mDatabase.mCommentsTableManager = new CommentsTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the comments Table Manager");
        }
        try {
            mDatabase.mUsersTableManager = new UsersTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the users Table Manager");
        }
        mDatabase.disconnect();
    }

    /**
     * test to see if this can set all the routes ok
     * the set up of routes is in the getDatabase() function
     */
    public void testRoutes() {
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        Database mDatabase = Database.getDatabase(db_url);
        assert (mDatabase != null);
        try {
            DatabaseRoutes.ideasRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of ideas table");
        }
        try {
            DatabaseRoutes.commentsRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of comments table");
        }
        try {
            DatabaseRoutes.userRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of users table");
        }
        try {
            DatabaseRoutes.likesRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of likes table");
        }
        try {
            DatabaseRoutes.dislikesRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of dislikes table");
        }
        try {
            DatabaseRoutes.loginRoutes(mDatabase);
        } catch (Exception e) {
            fail("Fail to set up the routes of login");
        }
        mDatabase.disconnect();
    }
}
