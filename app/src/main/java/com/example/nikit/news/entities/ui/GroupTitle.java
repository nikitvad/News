package com.example.nikit.news.entities.ui;

/**
 * Created by nikit on 20.04.2017.
 */

public class GroupTitle extends ListItem {

    private String title;

    public GroupTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return TITLE_ITEM;
    }
}
