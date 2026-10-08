package com.example.proyectoflan;

import javax.swing.plaf.nimbus.State;
import java.util.*;
import java.sql.*;
public class ManejadorPublish {
    /*public static ArrayList<Integer> allowedUsersIds;
    public static ArrayList<User> allowedUsers;

    public static void setAllowedUsersIds(ArrayList<User> allowedUsersIds) {
        ManejadorPublish.allowedUsers.clear();
        ManejadorPublish.allowedUsersIds.clear();

        for (User allowedUser : allowedUsersIds){
            ManejadorPublish.allowedUsers.add(allowedUser);
            ManejadorPublish.allowedUsersIds.add(allowedUser.getUserId());
        }
    }

    public static boolean createPost(Post post, Post parentPost) throws SQLException {
        String sql = "INSERT INTO POST (title, body, private, user_ID, post_parent_ID) " +
                    "VALUES (?, ?, ?, ?, ?)";


        try (Connection connection = ManejadorDB.abrirConexion(); PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getText());
            pstmt.setBoolean(3, post.isPriv());
            pstmt.setInt(4, post.getUserID());
            if (parentPost != null) pstmt.setInt(5, parentPost.getPostid());
            else pstmt.setNull(5, Types.INTEGER);

            int registrosAfectados = pstmt.executeUpdate();

            if (registrosAfectados > 0) {
                ResultSet llavesGeneradas = pstmt.getGeneratedKeys();
                if (llavesGeneradas.next()) {
                    int postId = llavesGeneradas.getInt(1);

                    if (post.isPriv() && post.getAllowedUsers() != null && !post.getAllowedUsers().isEmpty()) {
                        allowedUsersIds = (ArrayList<Integer>) post.getAllowedUsers();
                        addAllowedUsers(postId);
                    }

                    Statement st = ManejadorDB.abrirConexion().createStatement();
                    st.executeUpdate("INSERT INTO NOTIFICATION (title, body, user_ID, post_ID) VALUES ('Alguien respondio a tu publicacion!', 'body', '" + parentPost.getUserID() + "', '" + postId + "')");

                    return true;
                }
            }
            return false;
        }
    }

    private static void addAllowedUsers(int postId) throws SQLException {
        String sql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";

        try (Connection connection = ManejadorDB.abrirConexion(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (User allowedUser : allowedUsers) {
                pstmt.setInt(1, allowedUser.getUserId());
                pstmt.setInt(2, postId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private static void createNotificationForFollowers(Post post) throws SQLException {

        String sql = "INSERT INTO NOTIFICATION (title, body, user_ID, post_ID) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = ManejadorDB.abrirConexion(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "Nuevo post publicado");
            pstmt.setString(2, "Has publicado: " + post.getTitle());
            pstmt.setInt(3, post.getUserID());
            pstmt.setInt(4, post.getPostid());
            pstmt.executeUpdate();
        }
    }

    public static boolean updatePost(Post post) throws SQLException {
        String sql = "UPDATE POST SET title = ?, body = ?, edit = 1, private = ? " +
                "WHERE post_ID = ? AND user_ID = ?";

        try (Connection connection = ManejadorDB.abrirConexion(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getText());
            pstmt.setBoolean(3, post.isPriv());
            pstmt.setInt(4, post.getPostid());
            pstmt.setInt(5, post.getUserID());

            int registrosAfectados = pstmt.executeUpdate();

            if (registrosAfectados > 0 && post.isPriv()) {
                updateAllowedUsers(post.getPostid());
            }

            return registrosAfectados > 0;
        }
    }

    private static void updateAllowedUsers(int postId) throws SQLException {
        try (Connection connection = ManejadorDB.abrirConexion(); ){

            String deleteSql = "DELETE FROM VISIBILITY WHERE post_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                pstmt.setInt(1, postId);
                pstmt.executeUpdate();
            }

            if (allowedUsers != null && !allowedUsers.isEmpty()) {
                String insertSql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    for (User allowedUser : allowedUsers) {
                        pstmt.setInt(1, allowedUser.getUserId());
                        pstmt.setInt(2, postId);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

        }
    }

    public static List<User> getAllUsersForSelection(int currentUserId) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_ID, username FROM USURATO WHERE user_ID != ? ORDER BY username";

        try (Connection connection = ManejadorDB.abrirConexion(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                users.add(user);
            }
        }
        return users;
    }

    public static ArrayList<User> getAllowedUsersForPost(int postId){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM USURATO u " +
                "JOIN VISIBILITY v ON u.user_ID = v.user_ID " +
                "WHERE v.post_ID = ?";

        try (Connection conn = ManejadorDB.abrirConexion();PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }

    public static ArrayList<User> getExcludedUsersPorPost(int postId, int authorId){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM USURATO u " +
                "WHERE u.user_ID != ? " +
                "AND u.user_ID NOT IN (" +
                "   SELECT v.user_ID FROM VISIBILITY v WHERE v.post_ID = ?" +
                ")";

        try (Connection conn = ManejadorDB.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authorId);
            pstmt.setInt(2, postId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }

    public static ArrayList<User> getAllUsersExceptAuthor(int postId, int authorId){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USURATO WHERE user_ID != ?";

        try (Connection conn = ManejadorDB.abrirConexion();PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }*/

    public static boolean createPost(Post post, Post parentPost) throws SQLException {
        String sql = "INSERT INTO POST (title, body, private, user_ID, post_parent_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getText());
            pstmt.setBoolean(3, post.isPriv());
            pstmt.setInt(4, post.getUserID());

            if (parentPost != null) {
                pstmt.setInt(5, parentPost.getPostid());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            int registrosAfectados = pstmt.executeUpdate();

            if (registrosAfectados > 0) {
                ResultSet llavesGeneradas = pstmt.getGeneratedKeys();
                if (llavesGeneradas.next()) {
                    int postId = llavesGeneradas.getInt(1);

                    if (post.isPriv() && post.getAllowedUsers() != null && !post.getAllowedUsers().isEmpty()) {
                        addAllowedUsers(postId, post.getAllowedUsers());
                    }

                    if (parentPost != null) {
                        try (Statement st = connection.createStatement()) {
                            st.executeUpdate("INSERT INTO NOTIFICATION (title, body, user_ID, post_ID) " +
                                    "VALUES ('Alguien respondio a tu publicacion!', 'body', '" +
                                    parentPost.getUserID() + "', '" + postId + "')");
                        }
                    }

                    createNotificationForFollowers(post, postId);

                    return true;
                }
            }
            return false;
        }
    }

    private static void addAllowedUsers(int postId, List<Integer> allowedUserIds) throws SQLException {
        String sql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (Integer userId : allowedUserIds) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private static void createNotificationForFollowers(Post post, int postId) throws SQLException {
        String sql = "INSERT INTO NOTIFICATION (title, body, user_ID, post_ID) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, "Nuevo post publicado");
            pstmt.setString(2, "Has publicado: " + post.getTitle());
            pstmt.setInt(3, post.getUserID());
            pstmt.setInt(4, postId);
            pstmt.executeUpdate();
        }
    }

    public static boolean updatePost(Post post) throws SQLException {
        String sql = "UPDATE POST SET title = ?, body = ?, edit = 1, private = ? " +
                "WHERE post_ID = ? AND user_ID = ?";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getText());
            pstmt.setBoolean(3, post.isPriv());
            pstmt.setInt(4, post.getPostid());
            pstmt.setInt(5, post.getUserID());

            int registrosAfectados = pstmt.executeUpdate();

            if (registrosAfectados > 0) {
                if (post.isPriv()) {
                    updateAllowedUsers(post.getPostid(), post.getAllowedUsers());
                } else {
                    removeAllAllowedUsers(post.getPostid());
                }
            }

            return registrosAfectados > 0;
        }
    }

    private static void updateAllowedUsers(int postId, List<Integer> allowedUserIds) throws SQLException {
        System.out.println("IN MANEJADORPUBLISH");
        System.out.println("post id: " + postId);
        for (int i : allowedUserIds){
            System.out.println("user id: " + i);
        }
        try (Connection connection = ManejadorDB.abrirConexion()) {
            String deleteSql = "DELETE FROM VISIBILITY WHERE post_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                pstmt.setInt(1, postId);
                pstmt.executeUpdate();
            }

            if (allowedUserIds != null && !allowedUserIds.isEmpty()) {
                String insertSql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    for (Integer userId : allowedUserIds) {
                        pstmt.setInt(1, userId);
                        pstmt.setInt(2, postId);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }
        }
    }

    private static void removeAllAllowedUsers(int postId) throws SQLException {
        String sql = "DELETE FROM VISIBILITY WHERE post_ID = ?";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        }
    }

    public static List<User> getAllUsersForSelection(int currentUserId) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_ID, username FROM USURATO WHERE user_ID != ? ORDER BY username";

        try (Connection connection = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                users.add(user);
            }
        }
        return users;
    }

    public static ArrayList<User> getAllowedUsersForPost(int postId) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM USURATO u " +
                "JOIN VISIBILITY v ON u.user_ID = v.user_ID " +
                "WHERE v.post_ID = ?";

        try (Connection conn = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }

    public static ArrayList<User> getExcludedUsersPorPost(int postId, int authorId) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM USURATO u " +
                "WHERE u.user_ID != ? " +
                "AND u.user_ID NOT IN (" +
                "   SELECT v.user_ID FROM VISIBILITY v WHERE v.post_ID = ?" +
                ")";

        try (Connection conn = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authorId);
            pstmt.setInt(2, postId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }

    public static ArrayList<User> getAllUsersExceptAuthor(int postId, int authorId) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USURATO WHERE user_ID != ?";

        try (Connection conn = ManejadorDB.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("user_ID"));
                user.setUsername(rs.getString("username"));
                user.setDesc(rs.getString("bio"));
                user.setPfp("profile_picture");
                users.add(user);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return users;
    }
}
