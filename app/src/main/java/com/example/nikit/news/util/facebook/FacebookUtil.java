package com.example.nikit.news.util.facebook;

import com.example.nikit.news.entities.Friend_temp_name;
import com.example.nikit.news.entities.facebook.User;
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
    private static ArrayList<User> tempUserList;

    public static String getUidFromJson(JSONObject jsonObject) {
        try {

            return jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Friend_temp_name> getFriendsFromJson(JSONObject jsonObject) {
        Type ArrayListFriendType = new TypeToken<ArrayList<Friend_temp_name>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<Friend_temp_name> friends = new ArrayList<>();
        try {
            JSONObject object = jsonObject.getJSONObject("friends");
            JSONArray jsonArray = object.getJSONArray("data");

            friends = gson.fromJson(jsonArray.toString(), ArrayListFriendType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static ArrayList<User> getFacebookFriendsFromJson(JSONObject jsonObject) {
        Type ArrayListFriendType = new TypeToken<ArrayList<User>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONObject object = jsonObject.getJSONObject("friends");
            JSONArray jsonArray = object.getJSONArray("data");

            users = gson.fromJson(jsonArray.toString(), ArrayListFriendType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

}