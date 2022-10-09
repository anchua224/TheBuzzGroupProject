
package edu.lehigh.cse216.jub424.backend;

import spark.Spark;
// Import Google's JSON library
import com.google.gson.*;

import edu.lehigh.cse216.jub424.backend.data_request.*;
import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * DatabaseRoutes is to use the database connection to set up the routes
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class DatabaseRoutes {

    /**
     * set up all the routes for the ideas table
     * /ideas get, post
     * /ideas/:id get, put, delete
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
            SimpleIdeaRequest req = gson.fromJson(request.body(), SimpleIdeaRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = mDatabase.mIdeaTableManager.insertIdea(req.mTitle, req.mMessage);
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

        // DELETE route for removing a row from the table ideas
        Spark.delete("/ideas/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result_like = mDatabase.mLikeTableManager.deleteLikeIdea(idx);
            int result = mDatabase.mIdeaTableManager.deleteIdeas(idx);
            if (result == -1 || result_like == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // PUT route for updating a row in the ideas tabel. This is almost
        // exactly the same as POST
        Spark.put("/ideas/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleIdeaRequest req = gson.fromJson(request.body(), SimpleIdeaRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = mDatabase.mIdeaTableManager.updateIdea(idx, req.mTitle, req.mMessage);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

    }

    /**
     * set up all the routes for the likes table
     * /likes/:id get, post, delete
     * @param mDatabase connection of the database
     */
    public static void likesRoutes(Database mDatabase) {
        final Gson gson = new Gson();

        // POST route for adding a new element to the Ideas table. This will read
        // JSON from the body of the request, turn it into a SimpleIdeaRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        Spark.post("/likes/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = mDatabase.mLikeTableManager.likeIdea(idx);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // GET route that returns number of like of a idea that id correspond
        Spark.get("/likes/:id", (request, response) -> {
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
        Spark.delete("/likes/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result = mDatabase.mLikeTableManager.cancelLikeIdea(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    }
}