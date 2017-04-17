package com.example.nikit.news.firebase;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nikit.news.entities.News;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nikit on 28.03.2017.
 */

public class FirebaseDbHelper {
    FirebaseDatabase database;
    FirebaseAuth auth;

    public FirebaseDbHelper() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void pushLikedNews(News.Article article) {
        DatabaseReference reference = database.getReference("news");
        reference.child(article.getArticleId()).setValue(article.toMap());

        reference = database.getReference("users");
        reference.child(auth.getCurrentUser().getUid()).child("liked-news")
                .child(article.getArticleId()).setValue("true");

    }

    private News.Article article;

    public News.Article getLikedNews(String newsId) {
        DatabaseReference reference = database.getReference("news");
        reference.child(newsId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                article = dataSnapshot.getValue(News.Article.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return article;
    }

    public void removeLikedNews(String newsId) {
        DatabaseReference reference = database.getReference("users/" + auth.getCurrentUser().getUid() + "/liked-news");
        reference.child(newsId).removeValue();
    }

    public ArrayList<News.Article> getAllLikedNewses(HashMap<String, String> newsIds) {
        final ArrayList<News.Article> articles = new ArrayList<>();
        DatabaseReference reference;

        Iterator<Map.Entry<String, String>> iterator = newsIds.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> pair = iterator.next();
            reference = database.getReference("news/" + pair.getKey());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    article = dataSnapshot.getValue(News.Article.class);
                    if (article != null) {
                        articles.add(article);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return articles;
    }



}
