package com.example.nikit.news.util.facebook;

import android.os.Bundle;
import android.util.Log;

import com.example.nikit.news.entities.facebook.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikit on 15.04.2017.
 */

public class LoadUserInfo {
    private OnCompleteListener mListener;

    public LoadUserInfo(OnCompleteListener mListener) {
        this.mListener = mListener;
    }

    public static User getUserFromResponse(JSONObject object) {
        Gson gson = new Gson();
        if(object==null) return null;
            User user = gson.fromJson(object.toString(), User.class);
            String urlToAvatar = "";
            try {
                urlToAvatar = object.getJSONObject("picture").getJSONObject("data").getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.setUrlToAvatar(urlToAvatar);
            Log.d("LoadUserInfo", user.toString());

            return user;

    }

    public void load() {

        GraphRequest request = GraphRequest.newMeRequest(

                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoadUserInfo", response.toString());
                        if (mListener != null) {
                            mListener.onComplete(getUserFromResponse(object));
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void load(String openUid) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + openUid,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (mListener != null) {
                            mListener.onComplete(getUserFromResponse(response.getJSONObject()));
                        }
                    }
                }
        ).executeAsync();

    }

    public interface OnCompleteListener {
        void onComplete(User user);
    }
}
