package com.example.nikit.news.entities;

import com.example.nikit.news.entities.ui.RecycleListItem;

/**
 * Created by nikit on 10.04.2017.
 */

public class Friend_temp_name extends RecycleListItem {
    private String name;
    private long id;

    public Friend_temp_name() {
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return 0;
    }
}
