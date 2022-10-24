package edu.lehigh.cse216.jub424.backend.data_manager;
import java.sql.*;
public class DislikeTableManager {
    /**
     * A prepared statement for deleting a row from the database
     */
    private static PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private static PreparedStatement mInsertDislike;

    /**
     * A prepared statement for get dislike count
     */
    private static PreparedStatement mGetDislike;

    /**
     * A prepared statement for delete all dislike relative to an idea
     */
    private static PreparedStatement mDeleteIdeaDislike;

    public DislikeTableManager(Connection mConnection) throws SQLException{
        mDeleteOne = mConnection.prepareStatement("DELETE FROM dislikes WHERE user_id=? and id=?");
        mInsertDislike = mConnection.prepareStatement("INSERT INTO dislikes VALUES (?, ?)");
        mGetDislike = mConnection.prepareStatement("SELECT count(*) from dislikes WHERE id=?");
        mDeleteIdeaDislike = mConnection.prepareStatement("DELETE FROM dislikes WHERE id=?");
    }

    public int deleteOneDislike(String user_id, int id){
        int res = 0;
        try{
            mDeleteOne.setString(1, user_id);
            mDeleteOne.setInt(2,id);
            res = mDeleteOne.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public int inserOneDislike(String user_id, int id){
        int res = 0;
        try{
            mInsertDislike.setString(1, user_id);
            mInsertDislike.setInt(2, id);
            res = mInsertDislike.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    
    public int getDislikeCount(int id){
        int res = -1;
        try{
            mGetDislike.setInt(1,id);
            ResultSet rs = mGetDislike.executeQuery();
            if(rs.next()){
                res = rs.getInt("count");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    public int deleteDislike(int id){
        int res = 0;
        try{
            mDeleteIdeaDislike.setInt(1,id);
            res = mDeleteIdeaDislike.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }
}