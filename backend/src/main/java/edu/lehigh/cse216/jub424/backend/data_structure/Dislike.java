package edu.lehigh.cse216.jub424.backend.data_structure;
import java.util.Date;
/**
 * dislike holds a information of idea dislike situations. A row of dislike consists of
 * an idea_id, and user_id. A combination of idea_id and user_id is unique
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class Dislike {

    /**
     * id for user
     */

    public String user_id;
    /**
     * id for idea
     */
    public int idea_id;
    /**
     * the creation time, should not be changed
     */
    public final Date createdDate;

    /**
     * the constructor, takes in id of dislike and idea
     * 
     * @param user_id the id of user
     * @param idea_id the id of idea
     */
    public Dislike(String user_id, int idea_id){
        this.user_id=user_id;
        this.idea_id=idea_id;
        createdDate = new Date();
    }
}
