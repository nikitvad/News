package com.example.nikit.news.entities.firebase;

import com.example.nikit.news.entities.News;

import java.util.ArrayList;

/**
 * Created by nikit on 16.04.2017.
 */

public class NewsOfFriend {
    private String openUid;
    private ArrayList<News.Article> articles;

    public String getOpenUid() {
        return openUid;
    }

    public void setOpenUid(String openUid) {
        this.openUid = openUid;
    }

    public ArrayList<News.Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<News.Article> articles) {
        this.articles = articles;
    }
}
