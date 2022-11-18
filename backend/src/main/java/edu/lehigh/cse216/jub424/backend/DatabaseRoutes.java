
package edu.lehigh.cse216.jub424.backend;

import spark.Spark;

import java.util.ArrayList;

// Import Google's JSON library
import com.google.gson.*;

import edu.lehigh.cse216.jub424.backend.data_manager.OAuthManager;
import edu.lehigh.cse216.jub424.backend.data_manager.GoogleDriveManager;
import edu.lehigh.cse216.jub424.backend.data_request.*;
import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * DatabaseRoutes is to use the database connection to set up the routes
 * 
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class DatabaseRoutes {

    /**
     * set up all the routes for the ideas table
     * /ideas get, post
     * /ideas/:id get
     * 
     * @param mDatabase connection of the database
     */
    public static void ideasRoutes(Database mDatabase) {

        final Gson gson = new Gson();
        // GET route that returns all message titles and Ids with the like and dislike.
        // All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.
        Spark.get("/ideas", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null,
                    mDatabase.mIdeaTableManager.selectAllIdeas()));
        });

        // POST route for adding a new element to the Ideas table. This will read
        // JSON from the body of the request, turn it into a SimpleIdeaRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        Spark.post("/ideas", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            String sessionKey = request.queryParams("sessionKey");
            SimpleIdeaRequest req = gson.fromJson(request.body(), SimpleIdeaRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = mDatabase.mIdeaTableManager.insertIdea(req.mTitle, req.mMessage, 1, sessionKey);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // GET route that returns everything for a single row in the ideas table.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/ideas/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Idea idea = mDatabase.mIdeaTableManager.selectOneIdea(idx);
            if (idea == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found",
                        null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, idea));
            }
        });

        // // DELETE route for removing a row from the table ideas
        // Spark.delete("/ideas/:id", (request, response) -> {
        // // If we can't get an ID, Spark will send a status 500
        // int idx = Integer.parseInt(request.params("id"));
        // // ensure status 200 OK, with a MIME type of JSON
        // response.status(200);
        // response.type("application/json");
        // // NB: we won't concern ourselves too much with the quality of the
        // // message sent on a successful delete
        // int result_like = mDatabase.mLikeTableManager.deleteLikeIdea(idx);
        // int result = mDatabase.mIdeaTableManager.deleteIdeas(idx);
        // if (result == -1 || result_like == -1) {
        // return gson.toJson(new StructuredResponse("error", "unable to delete row " +
        // idx, null));
        // } else {
        // return gson.toJson(new StructuredResponse("ok", null, null));
        // }
        // });

        // // PUT route for updating a row in the ideas tabel. This is almost
        // // exactly the same as POST
        // Spark.put("/ideas/:id", (request, response) -> {
        // // If we can't get an ID or can't parse the JSON, Spark will send
        // // a status 500
        // int idx = Integer.parseInt(request.params("id"));
        // SimpleIdeaRequest req = gson.fromJson(request.body(),
        // SimpleIdeaRequest.class);
        // // ensure status 200 OK, with a MIME type of JSON
        // response.status(200);
        // response.type("application/json");
        // int result = mDatabase.mIdeaTableManager.updateIdea(idx, req.mTitle,
        // req.mMessage);
        // if (result == -1) {
        // return gson.toJson(new StructuredResponse("error", "unable to update row " +
        // idx, null));
        // } else {
        // return gson.toJson(new StructuredResponse("ok", null, result));
        // }
        // });

    }

    /**
     * set up all the routes for the likes table
     * /ideas/:id/likes?sessionKey get, post, delete
     * 
     * @param mDatabase connection of the database
     */
    public static void likesRoutes(Database mDatabase) {
        final Gson gson = new Gson();

        // POST route for adding a new element to the Ideas table. This will read
        // JSON from the body of the request, turn it into a SimpleIdeaRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        Spark.post("/ideas/:id/like", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String sessionKey = request.queryParams("sessionKey");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId;

            if (mDatabase.mDislikeTableManager.checkDislikeIdea(idx, sessionKey)) {
                int disR;
                disR = mDatabase.mDislikeTableManager.cancelDislikeIdea(idx, sessionKey);
                if (disR == -1) {
                    return gson.toJson(new StructuredResponse("error", "unable to cancel disliked record", null));
                }
            }
            if (!mDatabase.mLikeTableManager.checkLikeIdea(idx, sessionKey)) {
                newId = mDatabase.mLikeTableManager.likeIdea(idx, sessionKey);
            } else {
                newId = mDatabase.mLikeTableManager.cancelLikeIdea(idx, sessionKey);
            }
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing like", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // GET route that returns number of like of a idea that id correspond
        Spark.get("/ideas/:id/like", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int number = mDatabase.mLikeTableManager.getLikeCount(idx);
            if (number == -1) {
                return gson.toJson(new StructuredResponse("error", idx + " not found",
                        null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, number));
            }
        });

        // DELETE route for removing a like for a idea that with a id
        // this only remove one like
        Spark.delete("/ideas/:id/like", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            String userid = request.queryParams("sessionKey");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result = mDatabase.mLikeTableManager.cancelLikeIdea(idx, userid);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    }

    /**
     * set up all the routes for the dislikes table
     * /ideas/:id/dislikes?sessionKey get, post, delete
     * 
     * @param mDatabase connection of the database
     */
    public static void dislikesRoutes(Database mDatabase) {
        final Gson gson = new Gson();
        Spark.post("/ideas/:id/dislike", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String sessionKey = request.queryParams("sessionKey");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId;
            if (mDatabase.mLikeTableManager.checkLikeIdea(idx, sessionKey)) {
                int disR;
                disR = mDatabase.mLikeTableManager.cancelLikeIdea(idx, sessionKey);
                if (disR == -1) {
                    return gson.toJson(new StructuredResponse("error", "unable to cancel liked record", null));
                }
            }
            if (!mDatabase.mDislikeTableManager.checkDislikeIdea(idx, sessionKey)) {
                newId = mDatabase.mDislikeTableManager.dislikeIdea(idx, sessionKey);
            } else {
                newId = mDatabase.mDislikeTableManager.cancelDislikeIdea(idx, sessionKey);
            }
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing dislike", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // GET route that returns number of like of a idea that id correspond
        Spark.get("/ideas/:id/dislike", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int number = mDatabase.mDislikeTableManager.getDislikeCount(idx);
            if (number == -1) {
                return gson.toJson(new StructuredResponse("error", idx + " not found",
                        null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, number));
            }
        });

        // DELETE route for removing a like for a idea that with a id
        // this only remove one like
        Spark.delete("/ideas/:id/dislike", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            String userid = request.queryParams("sessionKey");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result = mDatabase.mDislikeTableManager.cancelDislikeIdea(idx, userid);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    }

    /**
     * set up all the routes for the OAuth login
     * /login post
     * 
     * @param mDatabase connection of the database
     */
    public static void loginRoutes(Database mDatabase) {
        final Gson gson = new Gson();
        Spark.post("/login", (request, response) -> {
            String tokenString = request.queryParams("token");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<String> result = OAuthManager.OAuthHandling(tokenString);
            if (result.get(1).contains("lehigh.edu")) {
                // System.out.println("user is from lehigh");
                String sessionKey = result.get(7);
                mDatabase.mUsersTableManager.insertOneUser(sessionKey, result.get(1),
                        result.get(2), "NO GI YET", "NO SO YET", "NO NOTE YET");
                // System.out.println("after sql");
                return gson.toJson(sessionKey);
            } else {
                return gson.toJson(new StructuredResponse("error", "User not from Lehigh", null));
            }

        });
    }

    /**
     * set up all the routes for the comments table
     * /ideas/:id/comment, get, post
     * /ideas/:id/comment/:comid get, put
     * 
     * @param mDatabase connection of the database
     */
    public static void commentsRoutes(Database mDatabase) {
        final Gson gson = new Gson();
        Spark.get("/ideas/:id/comment", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null,
                    mDatabase.mCommentsTableManager.selectAllComUnderOneIdea(idx)));
        });
        Spark.post("/ideas/:id/comment", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String sessionKey = request.queryParams("sessionKey");
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleCommentRequest req = gson.fromJson(request.body(), SimpleCommentRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = mDatabase.mCommentsTableManager.insertOneComment(idx, sessionKey, req.mContent);
            if (result != 0) {
                return gson.toJson(new StructuredResponse("ok", null, null));
            } else {
                return gson.toJson(
                        new StructuredResponse("error", "insert comment error " + idx + " " + sessionKey, null));
            }
        });
        Spark.get("/ideas/:id/comment/:comid", (request, response) -> {
            // int idx = Integer.parseInt(request.params("id"));
            int cmidx = Integer.parseInt(request.params("comid"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null,
                    mDatabase.mCommentsTableManager.selectOneComment(cmidx)));
        });
        Spark.put("/ideas/:id/comment/:comid", (request, response) -> {
            int cmidx = Integer.parseInt(request.params("comid"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            SimpleCommentRequest req = gson.fromJson(request.body(), SimpleCommentRequest.class);
            int result = mDatabase.mCommentsTableManager.updateOneComment(cmidx, req.mContent);
            if (result != 0) {
                return gson.toJson(new StructuredResponse("ok", null, null));
            } else {
                return gson.toJson(new StructuredResponse("error", "update comment error " + cmidx, null));
            }
        });
    }

    /**
     * set up all the routes for the users table
     * /profile/:userid, get, put
     * 
     * @param mDatabase connection of the database
     */
    public static void userRoutes(Database mDatabase) {
        final Gson gson = new Gson();
        Spark.get("/profile/:userid", (request, response) -> {
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // System.out.println("hello");
            User rUser = mDatabase.mUsersTableManager.selectOneUser(useridx);
            return gson.toJson(new StructuredResponse("ok", null, rUser));
        });
        Spark.put("/profile/:userid", (request, response) -> {
            String useridx = request.params("userid");
            SimpleUserRequest req = gson.fromJson(request.body(), SimpleUserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            // System.out.println("hello"+req.mGI);
            int result = mDatabase.mUsersTableManager.updateProfile(useridx, req.mName, req.mGI, req.mSO, req.mNote);
            if (result != 0) {
                return gson.toJson(new StructuredResponse("ok", null, null));
            } else {
                return gson.toJson(new StructuredResponse("error", "update user profile error " + useridx, null));
            }
        });
    }

    /**
     * set up all the routes for the users table
     * /resource/:id, get, post
     * 
     * @param mDatabase connection of the database
     */
    public static void resourceRoutes(Database mDatabase) {
        Gson gson = new Gson();
        Spark.get("/resource", (request, response) -> {
            int cmidx = Integer.parseInt(request.params("comid"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // TODO: create getResource method in ResourceManager
            // int resource = mDatabase.mGoogleDriveHandler.getResource(cmidx);
            // mDatabase.mGoogleDriveManager.GoogleDrive();
            return gson.toJson(new StructuredResponse("ok", null, 1));
            // return gson.toJson(new StructuredResponse("ok", null,
            // mDatabase.mGoogleDriveHandler.selectResource(cmidx))); //TODO: create select
            // resource method in ResourceManager.java
        });
    }

    public static void checkRoutes(Database mDatabase) {
        Gson gson = new Gson();
        Spark.get("/ideas/:id/checklike/:sessionKey", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String sessionKeyx = request.params("sessionKey");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            boolean like_result = mDatabase.mLikeTableManager.checkLikeIdea(idx, sessionKeyx);
            boolean dislike_result = mDatabase.mDislikeTableManager.checkDislikeIdea(idx, sessionKeyx);
            if (like_result) {
                return gson.toJson(new StructuredResponse("ok", null, 1));
            } else if (dislike_result) {
                return gson.toJson(new StructuredResponse("ok", null, -1));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, 0));
            }
        });
    }
}