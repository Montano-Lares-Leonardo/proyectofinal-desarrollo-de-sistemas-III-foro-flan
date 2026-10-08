package com.example.proyectoflan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private int postid;
    private String title;
    public String getDate(){
        if (edited) return "editado - " + datePosted;
        else return "" + datePosted;
    }

    private String text;
    private int author;
    private String username;
    private boolean priv;
    private Date datePosted;
    private boolean edited;
    private ArrayList<Integer> allowedUsers;

    public Post(){}

    public Post(int id, String title, String text, int author, String username, boolean priv, Date datePosted, boolean edited) {
        this.postid = id;
        this.title = title;
        this.text = text;
        this.author = author;
        this.username = username;
        this.priv = priv;
        this.datePosted = datePosted;
        this.edited = edited;
    }

    public int getUserID() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public int getPostid() {
        return postid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthor() {
        return author;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public List<Integer> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(ArrayList<Integer> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }
}
