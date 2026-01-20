package com.example.Webapp.Model;
import java.sql.Timestamp;

public class Article {
    private int article_id;
    private String account;
    private String article_title;
    private  String article_content;
    private  int  article_view;//浏览量
    private int  article_comments_count; //评论数
    private int  article_like;//点赞数
    private Timestamp article_date;//发布日期

    public Article() {
    }

    public Article(int article_id, String account, String article_title, String article_content, int article_view, int article_comments_count, int article_like, Timestamp article_date) {
        this.article_id = article_id;
        this.account = account;
        this.article_title = article_title;
        this.article_content = article_content;
        this.article_view = article_view;
        this.article_comments_count = article_comments_count;
        this.article_like = article_like;
        this.article_date = article_date;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public int getArticle_view() {
        return article_view;
    }

    public void setArticle_view(int article_view) {
        this.article_view = article_view;
    }

    public int getArticle_comments_count() {
        return article_comments_count;
    }

    public void setArticle_comments_count(int article_comments_count) {
        this.article_comments_count = article_comments_count;
    }

    public int getArticle_like() {
        return article_like;
    }

    public void setArticle_like(int article_like) {
        this.article_like = article_like;
    }

    public Timestamp getArticle_date() {
        return article_date;
    }

    public void setArticle_date(Timestamp article_date) {
        this.article_date = article_date;
    }

    @Override
    public String toString() {
        return "Article{" +
                "article_id=" + article_id +
                ", account='" + account + '\'' +
                ", article_title='" + article_title + '\'' +
                ", article_content='" + article_content + '\'' +
                ", article_view=" + article_view +
                ", article_comments_count=" + article_comments_count +
                ", article_like=" + article_like +
                ", article_date=" + article_date +
                '}';
    }
}
