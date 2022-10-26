package edu.lehigh.cse216.jub424.backend.data_manager;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.*;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

public class CommentsTableManager {
    private static PreparedStatement mSelectAll;

    private static PreparedStatement mSelectOne;

    private static PreparedStatement mUpdateOne;

    private static PreparedStatement mInsertOne;

    private static PreparedStatement mSelectAllUnderOneIDea;

    public CommentsTableManager(Connection mConnection) throws SQLException{
        mSelectAll = mConnection.prepareStatement("SELECT * FROM comments ORDER BY com_id DESC");
        mSelectOne = mConnection.prepareStatement("SELECT * FROM comments where com_id=?");
        mUpdateOne = mConnection.prepareStatement("UPDATE comments SET content=? where com_id=?");
        mInsertOne = mConnection.prepareStatement("INSERT INTO comments VALUES (?, ?, default, ?)");
        mSelectAllUnderOneIDea = mConnection.prepareStatement("SELECT * FROM comments where id=? ORDER BY com_id DESC");
    }

    public ArrayList<Comment> selectAllComments(){
        ArrayList<Comment> res = new ArrayList<>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new Comment(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getInt("com_id"),
                        rs.getString("content")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 

    public Comment selectOneComment(int com_id){
        Comment res = null;
        try {
            mSelectOne.setInt(1, com_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new Comment(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getInt("com_id"),
                        rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int updateOneComment(int com_id, String content){
        int res = -1;
        try {
            mUpdateOne.setString(1, content);
            mUpdateOne.setInt(2, com_id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int insertOneComment(int id, String user_id, String content){
        int res = 0;
        try {
            mInsertOne.setInt(1, id);
            mInsertOne.setString(2, user_id);
            mInsertOne.setString(3, content);
            res += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ArrayList<Comment> selectAllComUnderOneIdea(int id){
        ArrayList<Comment> res = new ArrayList<>();
        try{
            mSelectAllUnderOneIDea.setInt(1,id);
            ResultSet rs = mSelectAllUnderOneIDea.executeQuery();
            while(rs.next()){
                res.add(new Comment(
                    rs.getInt("id"),
                    rs.getString("user_id"),
                    rs.getInt("com_id"),
                    rs.getString("content"))
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
    
}
