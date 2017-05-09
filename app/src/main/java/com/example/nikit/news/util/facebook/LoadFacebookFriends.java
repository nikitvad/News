package com.example.nikit.news.util.facebook;

import android.os.Bundle;
import android.util.Log;

import com.example.nikit.news.entities.facebook.FacebookUser;
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

public class LoadFacebookFriends {
    ArrayList<FacebookUser> tempFacebookUserList;
    private OnProgressListener mListener;

    public LoadFacebookFriends(OnProgressListener mListener) {
        this.mListener = mListener;
    }

    public void load() {

        GraphRequest request = GraphRequest.newMeRequest(

                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoadFacebookUserInfo", response.toString());
                        tempFacebookUserList = FacebookUtil.getFacebookFriendsFromJson(object);
                        for (FacebookUser item : tempFacebookUserList) {
                            getAvatarToFriend(item);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void getAvatarToFriend(final FacebookUser facebookUser) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + facebookUser.getId() + "/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            facebookUser.setUrlToAvatar(response.getJSONObject().getJSONObject("data")
                                    .getString("url"));
                            if (mListener != null) {
                                mListener.onProgress(facebookUser);
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
        void onProgress(FacebookUser facebookUser);
    }
}

