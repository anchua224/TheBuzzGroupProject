package edu.lehigh.cse216.jub424.backend.data_structure;
import java.util.Date;

public class Dislike {
    /*
     * id for dislike
     */
    //public int dislike_id;

    public String user_id;
    /*
     * id for idea
     */
    public int idea_id;
    /*
     * the creation time, should not be changed
     */
    public final Date createdDate;

    /**
     * the constructor, takes in id of dislike and idea
     * 
     * @param dislike_id the id of dislike, should be unique
     * @param idea_id the id of idea
     */
    public Dislike(String user_id, int idea_id){
        this.user_id=user_id;
        this.idea_id=idea_id;
        createdDate = new Date();
    }
}
