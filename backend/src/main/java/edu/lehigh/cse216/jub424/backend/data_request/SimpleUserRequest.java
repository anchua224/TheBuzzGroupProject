package edu.lehigh.cse216.jub424.backend.data_request;
/**
 * SimpleRequest provides a format for clients to present name, GI, SO, and note
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */
public class SimpleUserRequest {
    /**
     * name of user provided by the client 
     */
    public String mName;
    /**
     * GI of user provided by the client 
     */
    public String mGI;
    /**
     * SO of user provided by the client 
     */
    public String mSO;
    /**
     * note of user provided by the client 
     */    
    public String mNote;
}
