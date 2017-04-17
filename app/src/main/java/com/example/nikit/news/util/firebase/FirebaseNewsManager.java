package com.example.nikit.news.util.firebase;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by nikit on 11.04.2017.
 */

public class FirebaseNewsManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static void pushNews(News.Article article) {
        DatabaseReference reference = database.getReference("news");
        reference.child(article.getArticleId()).setValue(article.toMap());
    }


    public static void shareNewsWithFriend(final String openUid, final SharedNews news) {

        DatabaseReference reference = database.getReference("users/openUID/" + openUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue().toString();



                Log.d("FirebaseNewsManager", uid);
                DatabaseReference ref = database.getReference("users/" + uid + "/news-of-friends");
                ref = ref.child(openUid).child(news.getNewsId());
                ref.setValue(news.toMap());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
