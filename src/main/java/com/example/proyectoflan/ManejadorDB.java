package com.example.proyectoflan;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class ManejadorDB {
    private static final String URL = "jdbc:mysql://localhost:3306/foroflan";
    private static final String USER = "forumuser";
    private static final String PASS = "iloveflan";

    public ManejadorDB() {
    }

    public static Connection abrirConexion() {
        try {
            DriverManager.setLoginTimeout(10);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
            return null;
        }
    }

    public static void cerrarConexion(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
        }
    }

    public static ArrayList<Post> getPosts(String condition) {
        try (Connection conn = abrirConexion()) {
            try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM POST p JOIN USURATO u ON u.user_ID = p.user_ID LEFT JOIN VISIBILITY v ON v.post_ID = p.post_ID WHERE ((v.user_ID = " + LoggedIn.getID() + ") OR (p.private = false))" + condition)) {
                ArrayList<Post> posts = new ArrayList<>();
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(7),
                            rs.getString(10),
                            rs.getBoolean(6),
                            rs.getDate(4),
                            rs.getBoolean(5)
                    ));
                }
                cerrarConexion(conn);
                return posts;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
            return null;
        }
    }

    public static ArrayList<User> getUsers(String condition) {
        try (Connection conn = abrirConexion()) {
            try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM USURATO u" + condition)) {
                ArrayList<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)
                    ));
                }
                cerrarConexion(conn);
                return users;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
            return null;
        }
    }

    public static boolean insertarUsuario(String name, String pass, String bio, String pfp) {
        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate("INSERT INTO USURATO (username, bio, profile_picture, password) VALUES (" + name + ", " + bio + ", " + pfp + ", " + pass + ")", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                LoggedIn.logIn(rs.getInt(1));
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "no se pudo guardar.\n" + e.getMessage(), "guardado NO exitoso >:(", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean editarUsuario(int id, String name, String pass, String bio, String pfp) {
        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {

            return 0 < stmt.executeUpdate("UPDATE USURATO SET username = " + name + ", bio = " + bio + ", profile_picture = " + pfp + ", password = " + pass + " WHERE user_ID = " + id);// devuelve numero de registros actualizados en la base de datos
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "no se pudo guardar.\n" + e.getMessage(), "guardado NO exitoso >:(", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean checkUser(String name, String password) {
        try (Connection conn = abrirConexion();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM USURATO u WHERE u.username = '" + name + "' AND u.password = '" + password + "'")) {
            boolean truth = rs.next();
            cerrarConexion(conn);
            return truth;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
            return false;
        }
    }

    public static ArrayList<Notif> getNotifs(String condition) {
        try (Connection conn = abrirConexion()) {
            try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM NOTIFICATION n JOIN POST p ON p.post_ID = n.post_ID JOIN USURATO u ON u.user_ID = p.user_ID WHERE n.user_ID = " + LoggedIn.getID() + condition + " ORDER BY n.is_read")) {
                ArrayList<Notif> notifs = new ArrayList<>();
                while (rs.next()) {
                    notifs.add(new Notif(
                            rs.getInt(1),
                            rs.getBoolean(2),
                            new Post(
                                    rs.getInt(6),
                                    rs.getString(8),
                                    rs.getString(9),
                                    rs.getInt(13),
                                    rs.getString(16),
                                    rs.getBoolean(13),
                                    rs.getDate(10),
                                    rs.getBoolean(11)
                            ), rs.getString(3)
                    ));
                }
                cerrarConexion(conn);
                return notifs;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de sql: " + e.getMessage() + "\nCodigo de error: " + e.getSQLState());
            return null;
        }
    }

    public static void setNotifRead(int notif) {
        String query = "UPDATE NOTIFICATION SET is_read=1 WHERE notification_ID=" + notif;
        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "no se pudo actualizar.\n" + e.getMessage(), "actualizacion NO exitosa >:(", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean deleteMyUser(int id) {
        String query = "DELETE FROM USURATO WHERE user_ID=" + id;
        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            return 0 < stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "no se pudo actualizar.\n" + e.getMessage(), "eliminacion NO exitosa >:(", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static Post getParentPost(int id) {
        try (Statement stmt = abrirConexion().createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT p.post_ID, p.title, p.body, u.user_ID, u.username, p.private, p.post_date, p.edit FROM POST AS p JOIN POST AS pr ON pr.post_parent_ID = p.post_ID JOIN USURATO u ON u.user_ID = p.user_ID LEFT JOIN VISIBILITY v ON v.post_ID = p.post_ID WHERE ((v.user_ID = " + LoggedIn.getID() + ") OR (p.private = false)) AND pr.post_ID = " + id);
            if (rs.next()) return new Post(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getBoolean(6),
                        rs.getDate(7),
                        rs.getBoolean(8));
            else return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /*public static Post getParent(int id) {
        System.out.println("SELECT post_parent_ID FROM POST WHERE post_ID = " + id);
        try (Connection conn = abrirConexion();
             ResultSet rs = conn.createStatement().executeQuery("SELECT post_parent_ID FROM POST WHERE post_ID = " + id)) {
            int parentID = rs.getInt(1);
            return getPosts(" AND p.post_ID = " + parentID).getFirst();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
