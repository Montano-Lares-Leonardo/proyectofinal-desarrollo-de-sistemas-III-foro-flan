package com.example.proyectoflan;

import com.example.proyectoflan.ManejadorDB;
import com.example.proyectoflan.User;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ManejadorPostAccess {
    public static ArrayList<User> getAllowedUsersForPost(int postId) {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "SELECT u.* FROM USURATO u " +
                    "JOIN VISIBILITY v ON u.user_ID = v.user_ID " +
                    "WHERE v.post_ID = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, postId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(
                                rs.getInt("user_ID"),
                                rs.getString("username"),
                                rs.getString("bio"),
                                rs.getString("profile_picture"),
                                rs.getString("password")
                        ));
                    }
                }
            }
            ManejadorDB.cerrarConexion(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener usuarios permitidos!!!: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
        }
        return users;
    }
    public static ArrayList<User> getExcludedUsersForPost(int postId, int authorId) {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "SELECT u.* FROM USURATO u " +
                    "WHERE u.user_ID != ? " +
                    "AND u.user_ID NOT IN (" +
                    "   SELECT v.user_ID FROM VISIBILITY v WHERE v.post_ID = ?" +
                    ")";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, authorId);
                pstmt.setInt(2, postId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(
                                rs.getInt("user_ID"),
                                rs.getString("username"),
                                rs.getString("bio"),
                                rs.getString("profile_picture"),
                                rs.getString("password")
                        ));
                    }
                }
            }
            ManejadorDB.cerrarConexion(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener usuarios excluidos!!!: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
        }
        return users;
    }

    public static ArrayList<User> getAllUsersExceptAuthor(int authorId) {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "SELECT * FROM USURATO WHERE user_ID != ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, authorId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(
                                rs.getInt("user_ID"),
                                rs.getString("username"),
                                rs.getString("bio"),
                                rs.getString("profile_picture"),
                                rs.getString("password")
                        ));
                    }
                }
            }
            ManejadorDB.cerrarConexion(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener usuarios: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
        }
        return users;
    }

    public static boolean addUserToWhitelist(int postId, int userId) {
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                int rowsAffected = pstmt.executeUpdate();
                ManejadorDB.cerrarConexion(conn);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al añadir usuario a whitelist: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
            return false;
        }
    }

    public static boolean removeUserFromWhitelist(int postId, int userId) {
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "DELETE FROM VISIBILITY WHERE user_ID = ? AND post_ID = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                int rowsAffected = pstmt.executeUpdate();
                ManejadorDB.cerrarConexion(conn);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar usuario de whitelist: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
            return false;
        }
    }

    public static boolean updatePostVisibility(int postId, ArrayList<User> allowedUsers) {
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String deleteSql = "DELETE FROM VISIBILITY WHERE post_ID = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, postId);
                deleteStmt.executeUpdate();
            }

            if (allowedUsers != null && !allowedUsers.isEmpty()) {
                allowedUsers.removeLast();
                String insertSql = "INSERT INTO VISIBILITY (user_ID, post_ID) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (User user : allowedUsers) {
                        insertStmt.setInt(1, user.getID());
                        insertStmt.setInt(2, postId);
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }

            ManejadorDB.cerrarConexion(conn);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar visibilidad: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
            return false;
        }
    }

    public static boolean isPostPrivate(int postId) {
        try (Connection conn = ManejadorDB.abrirConexion()) {
            String sql = "SELECT private FROM POST WHERE post_ID = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, postId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("private");
                    }
                }
            }
            ManejadorDB.cerrarConexion(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar post: " + e.getMessage() +
                            "\nCódigo de error: " + e.getSQLState());
        }
        return false;
    }
}