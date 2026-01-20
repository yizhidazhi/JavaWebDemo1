package com.example.Webapp.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String account;
    private String password;
    private  String name;
    private String gender;
    private int userid;
    private String avatar;
    private  String description;


    public User() {
    }

    public User(String account, String password, String name, String gender, int userid, String avatar, String description) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.userid = userid;
        this.avatar = avatar;
        this.description = description;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", userid=" + userid +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
