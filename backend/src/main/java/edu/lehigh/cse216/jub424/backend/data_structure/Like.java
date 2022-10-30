/**
 * like holds a information of idea like situations. A row of like consists of
 * an idea_id, and user_id. A combination of idea_id and user_id is unique
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */

package edu.lehigh.cse216.jub424.backend.data_structure;

import java.util.Date;

/**
 * Like hold the like messsage of theidea
 * each like id is unique and have a ideas id with them
 */
public class Like {
    /**
     * user_id, indeicate which user likes the idea
     */
    public int user_id;

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
     * Create a new Like with the provided idea_id, user_id and massage. And a
     * creation date based on the system clock at the time the constructor was
     * called.
     * 
     * @param user_id id to the like, which is unique for the whole time
     * 
     * @param idea_id id of the idea
     */
    public Like(int user_id, int idea_id) {
        this.user_id = user_id;
        this.idea_id = idea_id;
        createdDate = new Date();
    }
}
