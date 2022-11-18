package edu.lehigh.cse216.jub424.backend.data_structure;

import java.util.Date;

/**
 * Resource holds information about a resource.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class Resource {
    /**
     * a FK indicate the idea
     */
    public int idea_id;
    /**
     * the FK of the comment table,
     */
    public int com_id;
    /**
     * a FK indicate the user
     */
    public String user_id;
    /**
     * the PK of the comment table,
     */
    public int res_id;
    /**
     * the link for each resource
     */
    public String link;
    /**
     * the creation time, cannot be changed
     */
    public final Date createdDate;

    /**
     * constructor of the comment class, create a new comment with id of idea, user,
     * and comment.
     * Also provide the content of the comment
     * 
     * @param idea_id id of the idea which the resource related to
     * 
     * @param user_id id of the user who post the resource
     * 
     * @param com_id  id of the comment which the resource is related to
     * 
     * @param res_id  id of the resource, should be unique
     * 
     * @param link    the link for the resource
     * 
     */
    public Resource(int idea_id, int com_id, String user_id, int res_id, String link) {
        this.idea_id = idea_id;
        this.com_id = com_id;
        this.res_id = res_id;
        this.link = link;
        createdDate = new Date();
    }
}
