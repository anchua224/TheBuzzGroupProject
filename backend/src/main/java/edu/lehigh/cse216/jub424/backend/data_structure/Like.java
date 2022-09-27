/**
 * like holds a information of idea like situations. A row of liek consists of
 * an id which is idea_id, and unique like_id as PK.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */

package edu.lehigh.cse216.jub424.backend.data_structure;

import java.util.Date;

public class Like {
    /**
     * like_id, which is the primary key of like
     */
    public int like_id;

    /**
     * idea_id, which is indicate of the idea
     */
    public int idea_id;

     /**
     * The creation date for this row of data. Once it is set, it cannot be
     * changed
     */
    public final Date createdDate;

    /**
     * Create a new Idea with the provided id, title and massage. And a
     * creation date based on the system clock at the time the constructor was
     * called.
     * 
     * @param id id to the idea, which is unique for the whole time
     * 
     * @title The title of this idea
     * 
     * @param massage massage of the idea
     */
    public Like(int like_id, int idea_id) {
        this.like_id = like_id;
        this.idea_id = idea_id;
        createdDate = new Date();
    }
}
