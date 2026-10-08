package com.example.proyectoflan;

import java.util.ArrayList;

public class LoggedIn {
    private static User you;
    public static void logIn(int id){
        you = ManejadorDB.getUsers(" WHERE u.user_ID = " + id).getFirst();
    }
    public static User getYou(){
        return you;
    }

    public static int getID() {
        if (you == null) return 0;
        else return you.getID();
    }

    public static void logIn(User user){
        you = user;
    }

    public static void logOut(){
        you = null;
    }

    public static boolean loggedIn(){
        return you != null;
    }
}
