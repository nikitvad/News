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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nikit on 16.04.2017.
 */

public class LoadNewsesOfFriends {
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private OnProgressListener mListener;


    public LoadNewsesOfFriends(@Nullable OnProgressListener listener) {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mListener = listener;
    }

    public void getNewsesOfFriends() {
        if(firebaseAuth.getCurrentUser() == null ){
            return;
        }
        DatabaseReference reference = database.getReference("users/" + firebaseAuth.getCurrentUser().getUid() +
                "/news-of-friends");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, HashMap<String, SharedNews>>> t =
                        new GenericTypeIndicator<HashMap<String, HashMap<String, SharedNews>>>() {
                        };

                HashMap<String, HashMap<String, SharedNews>> hashMap = new HashMap<String, HashMap<String, SharedNews>>();
                hashMap = dataSnapshot.getValue(t);
                Log.d("getNewsesOfFriends", hashMap.toString());


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

    public interface OnProgressListener {
        void onProgress(SharedNews sharedNews);
    }
}
