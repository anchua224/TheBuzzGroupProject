package edu.lehigh.cse216.jub424.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

import java.util.Map;

/**
 * For now, our app creates an HTTP server that can deal with the ideas and like
 * 
 * @author Junchen Bao
 * @version 1.0.0
 * @since 2022-09-16
 */
public class App {
    /**
     * this is the funtion to run the app of the backend
     * 
     * @param args unsed
     */
    public static void main(String[] args) {

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse

        // dataStore holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        // every time we start the server, we'll have an empty dataStore,
        // with IDs starting over from 0.
        // final DataStore dataStore = new DataStore();
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        // postgres://idbttiaffnxknz:accfe21dd483005ea86f0a4c763a3c9efafeed22a95540ddd69b3aaacd7412ea@ec2-54-91-223-99.compute-1.amazonaws.com:5432/d8o35qeqah6p4p
        Database mDatabase = Database.getDatabase(ip, port, user, pass);
        if (mDatabase == null)
            return;

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 5432));

        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        String cors_enabled = env.get("CORS_ENABLED");

        if ("True".equalsIgnoreCase(cors_enabled)) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        DatabaseRoutes.ideasRoutes(mDatabase);
        DatabaseRoutes.likesRoutes(mDatabase);
        DatabaseRoutes.dislikesRoutes(mDatabase);
        DatabaseRoutes.loginRoutes(mDatabase);
        DatabaseRoutes.commentsRoutes(mDatabase);
        DatabaseRoutes.userRoutes(mDatabase);
        DatabaseRoutes.checkRoutes(mDatabase);
        DatabaseRoutes.resourceRoutes(mDatabase);
        // mDatabase.mLikeTableManager.dropTable();
        // mDatabase.mLikeTableManager.createTable();
        // mDatabase.mDislikeTableManager.dropTable();
        // mDatabase.mDislikeTableManager.createTable();
        // mDatabase.mCommentsTableManager.createTable();
        // mDatabase.mCommentsTableManager.dropTable();
        // mDatabase.mIdeaTableManager.dropTable();
        // mDatabase.mIdeaTableManager.createTable();
        // mDatabase.mDislikeTableManager.createTable();
        // mDatabase.mCommentsTableManager.createTable();
        // mDatabase.mLikeTableManager.createTable();
    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     * 
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }
}