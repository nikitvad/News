package com.example.nikit.news.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.nikit.news.Constants;
import com.example.nikit.news.api.ApiClient;
import com.example.nikit.news.entities.News;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by nikit on 20.03.2017.
 */

public class NetworkUtil {
    private Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }

    public static News getNewsFromSource(String sourceId, @Nullable String sortBy) {
        try {
            Response<News> response = ApiClient.getInstance()
                    .getNewsEntity(sourceId, sortBy, Constants.API_KEY).execute();

            if (response.isSuccessful() && response.body() != null) {
                for (News.Article article : response.body().getArticles()) {
                    article.setSourceId(sourceId);
                }
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
