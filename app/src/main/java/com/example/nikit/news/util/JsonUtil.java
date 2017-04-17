package com.example.nikit.news.util;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.Source;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nikit on 12.03.2017.
 */

public class JsonUtil {
    //private static String jsonString;
    public static String loadJsonString(String urlAddress) {
        //new LoadJsonAsyncTask().execute(urlAddress);
        String jsonStringResult = "";

        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            jsonStringResult = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStringResult;
    }


    public static ArrayList<Source> getSourcesFromJson(String jsonString) {
        String sourcesJsonString;
        ArrayList<Source> sourcesResult;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            sourcesJsonString = jsonObject.getJSONArray("sources").toString();
            if (sourcesJsonString != null && sourcesJsonString.length() > 0) {
                Gson gson = new Gson();
                Type ArrayListSourceType = new TypeToken<ArrayList<Source>>() {
                }.getType();
                sourcesResult = gson.fromJson(sourcesJsonString, ArrayListSourceType);

                if (sourcesResult.size() > 0) {
                    return sourcesResult;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static News getNewsFromJsonString(String jsonString) {
        News news = new News();
        if (jsonString != null) {
            Gson gson = new Gson();
            news = gson.fromJson(jsonString, News.class);
            return news;
        } else return null;
    }

}
