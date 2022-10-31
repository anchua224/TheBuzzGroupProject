package edu.lehigh.cse216.jub424.backend.data_manager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import edu.lehigh.cse216.jub424.backend.Hashing.HashFunc;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import java.util.Collections;

/**
 * OAuthTableManager contains a function that helps process token
 * @author Na Chen
 * @version 1.0.0
 * @since 2022-10-28
 */
public class OAuthManager {
    public static final String CLIENT_ID = "259939040609-4gh5cug157ecmc3igr6qtpqojjl6813g.apps.googleusercontent.com";

    /**
     * OAuthHandling takes in a token id string and return user information and sessionKey
     * @param idTokenString
     * @return An arrayList that contains user information from google and sessionKey
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static ArrayList<String> OAuthHandling(String idTokenString) throws GeneralSecurityException, IOException{
        ArrayList<String> userInfo = new ArrayList<>();
        JsonFactory jsonFactory = new GsonFactory();
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        // Specify the CLIENT_ID of the app that accesses the backend:
        .setAudience(Collections.singletonList(CLIENT_ID))
        // Or, if multiple clients access the backend:
        //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
        .build();
    
        // (Receive idTokenString by HTTPS POST)
    
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
    
            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);
    
            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            //System.out.println("success");
         
            userInfo.add(userId);
            userInfo.add(email);
            userInfo.add(name);
            userInfo.add(pictureUrl);
            userInfo.add(locale);
            userInfo.add(familyName);
            userInfo.add(givenName);
            
            String hashedSessionKey = HashFunc.hash(email);
            userInfo.add(hashedSessionKey);
            // Use or store profile information、
            //System.out.println("success after hash: " + hashedSessionKey);
            // ...
    
        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
        return userInfo;
    }
}
