package com.example.nikit.news.entities.firebase;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.facebook.User;
import com.example.nikit.news.entities.ui.ListItem;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by nikit on 16.04.2017.
 */

public class SharedNews extends ListItem {
    public static final String NEWS_TYPE_DEF = "def";
    public static final String NEWS_TYPE_NEW = "new";

    private String newsId;
    private String comment;
    private String date;
    private String newsType;
    private News.Article article;
    private User user;

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public SharedNews() {
        date = new Date().toString();
        newsType = NEWS_TYPE_NEW;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public News.Article getArticle() {
        return article;
    }

    public void setArticle(News.Article article) {
        this.article = article;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("newsId", newsId);
        hashMap.put("comment", comment);
        hashMap.put("date", date.toString());
        hashMap.put("newsType", newsType);
        return hashMap;
    }

    @Override
    public String toString() {
        return "SharedNews{" +
                "newsId='" + newsId + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", newsType='" + newsType + '\'' +
                ", article=" + article +
                ", user=" + user +
                '}';
    }

    @Override
    public int getType() {
        return DEF_ITEM;
    }
}
