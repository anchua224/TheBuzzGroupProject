package edu.lehigh.cse216.jub424.backend.data_structure;
import java.util.Date;

public class Comment {
    /*
     * a FK indicate the idea
     */
    public int idea_id;
    /*
     * a FK indicate the user
     */
    public String user_id;
    /*
     * the PK of the comment table, 
     */
    public int com_id;
    /*
     * the content of each comment
     */
    public String content;
    /*
     * the creation time, cannot be changed 
     */
    public final Date createdDate;
    /**
     * constructor of the comment class, create a new comment with id of idea, user, and comment.
     * Also provide the content of the comment
     * 
     * @param idea_id id of the idea which the comment related to
     * 
     * @param user_id id of the user who post the comment
     * 
     * @param com_id id of the comment, should be unique
     * 
     * @param content the content of the comment
     *
     */
    public Comment(int idea_id, String user_id, int com_id, String content){
        this.idea_id = idea_id;
        this.user_id = user_id;
        this.com_id = com_id;
        this.content = content;
        createdDate = new Date();
    }
}
