package com.example.nikit.news.util.firebase;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.facebook.User;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.util.facebook.LoadUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nikit on 16.04.2017.
 */

public class LoadNewsesFromFriends {

    private static final String TAG = LoadNewsesFromFriends.class.getSimpleName();
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    //private OnProgressListener mListener;

    public LoadNewsesFromFriends(@Nullable OnProgressListener listener) {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //mListener = listener;

    }

    public LoadNewsesFromFriends() {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loadAll(final OnProgressListener mListener) {
        if (firebaseAuth.getCurrentUser() == null) {
            return;
        }
        DatabaseReference reference = database.getReference("users/" + firebaseAuth.getCurrentUser().getUid() +
                "/news-of-friends/all");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Log.e(TAG, "no data error");
                    return;
                }
                GenericTypeIndicator<HashMap<String, HashMap<String, SharedNews>>> t =
                        new GenericTypeIndicator<HashMap<String, HashMap<String, SharedNews>>>() {
                        };

                HashMap<String, HashMap<String, SharedNews>> hashMap;
                hashMap = dataSnapshot.getValue(t);

                Iterator<Map.Entry<String, HashMap<String, SharedNews>>> iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<String, HashMap<String, SharedNews>> pair = iterator.next();

                    new FirebaseLoadNews(new FirebaseLoadNews.OnProgressListener() {
                        @Override
                        public void onProgress(News.Article article) {
                            final SharedNews sharedNews = pair.getValue().get(article.getArticleId());

                            sharedNews.setArticle(article);
                            new LoadUserInfo(new LoadUserInfo.OnCompleteListener() {
                                @Override
                                public void onComplete(User user) {
                                    sharedNews.setUser(user);
                                    if (mListener != null) {
                                        mListener.onProgress(sharedNews);
                                        if(sharedNews.getNewsType().equals(sharedNews.NEWS_TYPE_NEW)){
                                            dataSnapshot.getRef().child(user.getId()).child(sharedNews.getNewsId())
                                                    .child("newsType").setValue(SharedNews.NEWS_TYPE_DEF);
                                        }
                                    }
                                    Log.d("getNewsesOfFriends111", sharedNews.toString());

                                }
                            }).load(pair.getKey());

                        }
                    }).load(pair.getValue().keySet());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadNew() {
        final DatabaseReference referenceToNew = database.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        HashMap<String, String> newsIdOwnerId = new HashMap<>();

        referenceToNew.child("news-of-friends/new").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};

                    hashMap = dataSnapshot.getValue(t);
                    Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, String> pair = iterator.next();

                        referenceToNew.child("news-of-friends/all").child(pair.getValue())
                                .child(pair.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d("loadNew", dataSnapshot.toString());
                                if (dataSnapshot.getValue() != null) {
                                    SharedNews sharedNews = dataSnapshot.getValue(SharedNews.class);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface OnProgressListener {
        void onProgress(SharedNews sharedNews);
    }
}
