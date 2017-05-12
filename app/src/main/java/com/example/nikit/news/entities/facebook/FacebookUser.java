package com.example.nikit.news.entities.facebook;

import java.util.HashMap;

/**
 * Created by nikit on 15.04.2017.
 */

public class FacebookUser {
    private String id;
    private String name;
    private String urlToAvatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlToAvatar() {
        return urlToAvatar;
    }

    public void setUrlToAvatar(String urlToAvatar) {
        this.urlToAvatar = urlToAvatar;
    }

    @Override
    public String toString() {
        return "FacebookUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", urlToAvatar='" + urlToAvatar + '\'' +
                '}';
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("urlToAbatar", urlToAvatar);
        return result;
    }
}
