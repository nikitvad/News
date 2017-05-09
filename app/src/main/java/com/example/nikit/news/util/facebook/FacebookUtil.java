package com.example.nikit.news.util.facebook;

import com.example.nikit.news.entities.facebook.FacebookUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by nikit on 13.04.2017.
 */

public class FacebookUtil {
    private static ArrayList<FacebookUser> tempFacebookUserList;

    public static ArrayList<FacebookUser> getFacebookFriendsFromJson(JSONObject jsonObject) {
        Type ArrayListFriendType = new TypeToken<ArrayList<FacebookUser>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<FacebookUser> facebookUsers = new ArrayList<>();
        try {
            JSONObject object = jsonObject.getJSONObject("friends");
            JSONArray jsonArray = object.getJSONArray("data");

            facebookUsers = gson.fromJson(jsonArray.toString(), ArrayListFriendType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return facebookUsers;
    }

}
