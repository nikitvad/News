package com.example.nikit.news.entities.ui;

/**
 * Created by nikit on 11.04.2017.
 */

public class GroupHeader extends RecycleListItem {
    private String userName;
    private String urlToAvatar;
    private String openUid;

    public String getOpenUid() {
        return openUid;
    }

    public void setOpenUid(String openUid) {
        this.openUid = openUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrlToAvatar() {
        return urlToAvatar;
    }

    public void setUrlToAvatar(String urlToAvatar) {
        this.urlToAvatar = urlToAvatar;
    }

    @Override
    public int getType() {
        return 0;
    }
}
