package edu.lehigh.cse216.jub424.backend.data_structure;

import java.util.Date;

/**
 * Idea holds a post of idea. A row of idea consists of
 * an identifier, strings for a "title" and "content", and a creation date.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */

public class Idea {

    /**
     * id, which is the primary key of idea
     */
    public int id;

    /**
     * The title for this idea
     */
    public String title;

    /**
     * massage in the idea which is a string
     */
    public String massage;

    /**
     * the like counter of the idea
     */
    public int like;

    /**
     * the dislike counter of the idea
     */
    public int dislike;

    /**
     * The creation date for this row of data. Once it is set, it cannot be
     * changed
     */
    public final Date createdDate;

    /**
     * Create a new Idea with the provided id, title, massage, like and dislike. And
     * a creation date based on the system clock at the time the constructor was
     * called
     * 
     * @param id id to the idea, which is unique for the whole time
     * 
     * @title The title of this idea
     * 
     * @param massage massage of the idea
     * 
     * @param like    like count
     * 
     * @param dislike The dislike count
     */
    public Idea(int id, String title, String massage, int like, int dislike) {
        this.id = id;
        this.title = title;
        this.massage = massage;
        this.like = like;
        this.dislike = dislike;
        createdDate = new Date();
    }

    /**
     * Create a new Idea with the provided id, title and massage. And a
     * creation date based on the system clock at the time the constructor was
     * called. the like and dislike will be 0
     * 
     * @param id id to the idea, which is unique for the whole time
     * 
     * @title The title of this idea
     * 
     * @param massage massage of the idea
     */
    public Idea(int id, String title, String massage) {
        this.id = id;
        this.title = title;
        this.massage = massage;
        this.like = 0;
        this.dislike = 0;
        createdDate = new Date();
    }

}
