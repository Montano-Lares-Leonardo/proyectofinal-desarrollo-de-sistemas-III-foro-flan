package com.example.proyectoflan;

public class Notif {
    private final int id;
    private boolean opened;
    private Post post;
    private String cause;

    public Notif(int id, boolean opened, Post post, String cause) {
        this.id = id;
        this.opened = opened;
        this.cause = cause;
        this.post = post;
    }

    public String getCause() {
        return cause;
    }

    public String getTitle(){
        return post.getTitle();
    }

    public String getRead(){
        if (opened) return "           ☑";
        else return "           ☐";
    }

    public Post getPost(){
        return post;
    }

    public int getId() {
        return id;
    }
}