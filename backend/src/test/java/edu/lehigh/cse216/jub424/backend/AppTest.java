package edu.lehigh.cse216.jub424.backend;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.FileUtils;

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
        Idea idea = new Idea(id, title, content, 1, user_id);

        assertTrue(idea.id == id);
        assertTrue(idea.title.equals(title));
        assertTrue(idea.massage.equals(content));
        assertTrue(idea.userid.equals(user_id));
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
        User user = new User(user_id, email, name, GI, SO, note, 1);

        assertTrue(user.user_id.equals(user_id));
        assertTrue(user.email.equals(email));
        assertTrue(user.name.equals(name));
        assertTrue(user.GI.equals(GI));
        assertTrue(user.SO.equals(SO));
        assertTrue(user.note.equals(note));
        assertTrue(user.validity == 1);
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
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testResourceConstructor() {
        int idea_id = 4000;
        int com_id = 2000;
        String user_id = "qwertyuiopasdfghjklzxcvbnm1234567890opuytrewqasdfghjklzxcvbnm34";
        int res_id = 1000;
        String link = "https://upload.wikimedia.org/wikipedia/en/thumb/6/65/LehighMountainHawks.svg/1200px-LehighMountainHawks.svg.png";
        int validity = 1;
        Resource resource = new Resource(idea_id, com_id, user_id, res_id, link,
                validity);

        assertTrue(resource.idea_id == idea_id);
        assertTrue(resource.com_id == com_id);
        assertTrue(resource.user_id.equals(user_id));
        assertTrue(resource.res_id == res_id);
        assertTrue(resource.link.equals(link));
        assertTrue(resource.validity == validity);
    }

    /**
     * test for google drive service account connection
     */
    public void testGoogeDriveConnection() {
        try {
            GoogleDriveManager.quickStart();
        } catch (IOException e) {
            fail("IOException, failed to get credentials");
        } catch (GeneralSecurityException e) {
            fail("GeneralSecurityException, failed to connect to Google Drive");
        } catch (Exception e) {
            fail("Could not load credentials: " + e.getMessage());
        }
    }

    /**
     * test if a file can be uploaded to the service account drive
     *
     * @throws Exception
     */
    public void testFileUpload() throws Exception {
        int idea_id = 4000;
        int com_id = 2000;
        String user_id = "qwertyuiopasdfghjklzxcvbnm1234567890opuytrewqasdfghjklzxcvbnm34";
        int res_id = 1000;
        String filePath = "./src/main/java/resources/image.png";
        File inputFile = new File(filePath);
        // byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        // String encoded = Base64.getEncoder().encodeToString(fileContent);
        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64
                .getEncoder()
                .encodeToString(fileContent);
        String fileMIME = "image/png";
        String filename = "image.png";
        if (encodedString == null)
            fail("Failed to encode file");
        int validity = 1;
        try {
            String link = GoogleDriveManager.uploadBasic(encodedString, fileMIME,
                    filename, idea_id, com_id);
            if (link == null) {
                throw new Exception("No link for file " + link);
            }
            Resource resource = new Resource(idea_id, com_id, user_id, res_id, link,
                    validity);
            assertTrue(resource.idea_id == idea_id);
            assertTrue(resource.com_id == com_id);
            assertTrue(resource.user_id.equals(user_id));
            assertTrue(resource.res_id == res_id);
            assertTrue(resource.link.equals(link));
            assertTrue(resource.validity == validity);
        } catch (IOException e) {
            fail("Failed to upload resource " + filePath);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * test if it can connect to the elephantSQL database
     *
     * @throws SQLException
     */
    public void testConnection() {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port +
                    "/", user, pass);
            if (conn == null) {
                fail("Error: DriverManager.getConnection() returned a null object");
            }
            conn.close();
        } catch (SQLException e) {
            fail("connect fail because the SQLException" + e.getMessage());
        } catch (ClassNotFoundException cnfe) {
            fail("Unable to find postgresql driver");
        }
    }

    /**
     * test to see if this can set all the manager ok
     * the set up of manager is in the getDatabase() function
     */
    public void testManager() {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        Database mDatabase = Database.getDatabase(ip, port, user, pass);
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
        try {
            mDatabase.mResourceTableManager = new ResourceTableManager(mDatabase.mConnection);
        } catch (Exception e) {
            fail("not able to set up the resource Table Manager");
        }
        mDatabase.disconnect();
    }

    /**
     * test to see if this can set all the routes ok
     * the set up of routes is in the getDatabase() function
     */
    public void testRoutes() {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        Database mDatabase = Database.getDatabase(ip, port, user, pass);
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
