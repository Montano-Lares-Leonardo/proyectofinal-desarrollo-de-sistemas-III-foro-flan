package com.example.proyectoflan;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private int userid;
    private String username;
    private String desc;
    private String pfp;
    private String password;

    public User(){}

    public User(int userid, String username, String desc, String pfp, String password) {
        this.userid = userid;
        this.username = username;
        this.desc = desc;
        this.pfp = pfp;
        this.password = password;
    }

    public int getID() {
        return userid;
    }

    public String getPfp() {
        return pfp;
    }

    public String getUsername() {
        return username;
    }

    public String getDesc(){
        return desc;
    }

    public String getPassword(){
        return password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Integer getUserId() {
        return userid;
    }
}
