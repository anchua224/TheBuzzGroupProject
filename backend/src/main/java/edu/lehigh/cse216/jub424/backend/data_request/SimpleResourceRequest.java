package edu.lehigh.cse216.jub424.backend.data_request;

/**
 * SimpleRequest provides a format for clients to present content of comment
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */
public class SimpleResourceRequest {
    /**
     * The link being provided by the client.
     */
    public String mLink;
}
