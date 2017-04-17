package com.example.nikit.news.entities.ui;

/**
 * Created by nikit on 11.04.2017.
 */

public abstract class RecycleListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ARTICLE = 1;
    abstract public int getType();
}
