package com.example.proyectoflan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManejadorPost {
    //private Connection conn;

    public static boolean deletePost(int postId){
        String sql = "DELETE FROM POST WHERE post_ID = ?";

        try(Connection conn = ManejadorDB.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,postId);
            int registrosAfectados = ps.executeUpdate();
            return registrosAfectados>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Post obtenerPostPorId(int postId){
        String sql = "SELECT * FROM POST WHERE post_ID = ?";

        try (Connection conn = ManejadorDB.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Post post = new Post();
                post.setPostid(rs.getInt("post_ID"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("body"));
                post.setDatePosted(rs.getTimestamp("post_date"));
                post.setEdited(rs.getBoolean("edit"));
                post.setPriv(rs.getBoolean("private"));
                return post;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static User getPostAutor(int postId){
        String sql = "SELECT u.* FROM USURATO u " +
                "JOIN POST p ON u.user_ID = p.user_ID " +
                "WHERE p.post_ID = ?";

        try (Connection conn = ManejadorDB.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPassword(rs.getString("password"));
                user.setPfp(rs.getString("profile_picture"));
                return user;
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return null;
    }

    public List<Post> getPublicPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM POST WHERE private = 0 ORDER BY post_date DESC";

        try (Connection conn = ManejadorDB.abrirConexion();Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Post post = new Post();
                post.setPostid(rs.getInt("post_ID"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("body"));
                post.setDatePosted(rs.getTimestamp("post_date"));
                post.setEdited(rs.getBoolean("edit"));
                post.setPriv(rs.getBoolean("private"));
                posts.add(post);
            }
        }
        return posts;
    }

    public List<Post> getPublicPostsPorUsuario(int userId){
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM POST p " +
                "LEFT JOIN VISIBILITY v ON p.post_ID = v.post_ID " +
                "WHERE p.private = 0 " +
                "OR (p.private = 1 AND v.user_ID = ?) " +
                "OR p.user_ID = ? " +
                "ORDER BY p.post_date DESC";

        try (Connection conn = ManejadorDB.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setPostid(rs.getInt("post_ID"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("body"));
                post.setDatePosted(rs.getTimestamp("post_date"));
                post.setEdited(rs.getBoolean("edit"));
                post.setPriv(rs.getBoolean("private"));
                posts.add(post);
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return posts;
    }


}
