package com.example.nikit.news.util.facebook;

import android.os.Bundle;
import android.util.Log;

import com.example.nikit.news.entities.facebook.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nikit on 15.04.2017.
 */

public class LoadUserFriends {
    ArrayList<User> tempUserList;
    private OnProgressListener mListener;

    public LoadUserFriends(OnProgressListener mListener) {
        this.mListener = mListener;
    }

    public void load() {

        GraphRequest request = GraphRequest.newMeRequest(

                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoadUserInfo", response.toString());
                        tempUserList = FacebookUtil.getFacebookFriendsFromJson(object);
                        for (User item : tempUserList) {
                            getAvatarToFriend(item);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void getAvatarToFriend(final User user) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + user.getId() + "/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            user.setUrlToAvatar(response.getJSONObject().getJSONObject("data")
                                    .getString("url"));
                            if (mListener != null) {
                                mListener.onProgress(user);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            /* handle the result */
                    }
                }
        ).executeAsync();
    }

    public interface OnProgressListener {
        void onProgress(User user);
    }
}

