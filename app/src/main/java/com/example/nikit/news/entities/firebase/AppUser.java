package com.example.nikit.news.entities.firebase;

import java.util.HashMap;

/**
 * Created by nikit on 09.05.2017.
 */

public class AppUser {
    private String id;
    private String name;
    private String email;
    private String urlToPhoto;

    public AppUser(com.google.firebase.auth.FirebaseUser user) {
        this.id = user.getUid();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.urlToPhoto = user.getPhotoUrl().toString();
    }

    public AppUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlToPhoto() {
        return urlToPhoto;
    }

    public void setUrlToPhoto(String urlToPhoto) {
        this.urlToPhoto = urlToPhoto;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        if (name != null) hashMap.put("name", name);
        if (email != null) hashMap.put("email", email);
        if (urlToPhoto != null) hashMap.put("urlToPhoto", urlToPhoto);
        return hashMap;
    }
}
