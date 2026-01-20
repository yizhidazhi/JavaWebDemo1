package com.example.Webapp.Model;

import java.sql.Timestamp;

public class Comment {

    private int id;
    private int comments_section_id;
    private int user_id;
    private int to_user_id;
    private int root_id;
    private String content;
    private Timestamp create_time;


    public Comment() {
    }

    public Comment(int id, int comments_section_id, int user_id, int to_user_id, int root_id, String content, Timestamp create_time) {
        this.id = id;
        this.comments_section_id = comments_section_id;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.root_id = root_id;
        this.content = content;
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComments_section_id() {
        return comments_section_id;
    }

    public void setComments_section_id(int comments_section_id) {
        this.comments_section_id = comments_section_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public int getRoot_id() {
        return root_id;
    }

    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comments_section_id=" + comments_section_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", root_id=" + root_id +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
