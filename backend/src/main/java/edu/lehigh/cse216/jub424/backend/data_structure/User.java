package edu.lehigh.cse216.jub424.backend.data_structure;

import java.util.Date;

/**
 * User holds information of one user.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class User {
    /**
     * id of the user
     */
    public String user_id;
    /**
     * email of the user
     */
    public String email;
    /**
     * name of the user
     */
    public String name;
    /**
     * GI of the user
     */
    public String GI;
    /**
     * SO of the user
     */
    public String SO;
    /**
     * note of the user
     */
    public String note;
    /**
     * validity of the user
     */
    public int validity;
    /**
     * the creation time, should not be changed
     */
    public final Date createdDate;

    /**
     * constructor of the user, create a user with id, email, name, GI, SO, and note
     * 
     * @param user_id  id of the user
     * @param email    email of the user
     * @param name     name of the user
     * @param GI       GI of the user
     * @param SO       SO of the user
     * @param note     note from the user
     * @param validity validity of the user
     * 
     */
    public User(String user_id, String email, String name, String GI, String SO, String note, int validity) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.GI = GI;
        this.SO = SO;
        this.note = note;
        this.validity = validity;
        createdDate = new Date();
    }
}
