package com.example.nikit.news.util.firebase;

import android.database.sqlite.SQLiteDatabase;
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


import java.util.Date;

import com.example.nikit.news.util.firebase.FirebaseConstants;

import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES_COUNT;

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


    public static void shareNewsWithFriend(final String authorUid, final String openUid, final SharedNews news) {
        DatabaseReference reference = database.getReference("users/openUID/" + openUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue().toString();

                Log.d("FirebaseNewsManager", uid);
                DatabaseReference ref = database.getReference("users/" + uid + "/news-of-friends/all");
                ref = ref.child(authorUid).child(news.getNewsId());
                ref.setValue(news.toMap());

                ref = database.getReference("users/" + uid + "/news-of-friends/new");
                ref.child(news.getNewsId()).setValue(authorUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getCountNewSharedNewses(final OnResultListener listener) {
        DatabaseReference reference = database.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        reference.child("news-of-friends/new").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long count = dataSnapshot.getChildrenCount();
                    if (listener != null) {
                        listener.onResult(count);
                        dataSnapshot.getRef().removeValue();
                    }
                } else {
                    if (listener != null) {
                        listener.onResult(0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void likeNews(final News.Article article, final OnSuccessListener listener) {

        DatabaseReference reference = database.getReference("news/" + article.getArticleId());
        reference.child(FB_REF_LIKES_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("likeNews", dataSnapshot.toString());

                if (dataSnapshot.getValue() != null) {
                    int likesCount = dataSnapshot.getValue(int.class);
                    dataSnapshot.getRef().setValue(likesCount + 1);
                } else {
                    pushNews(article);
                    dataSnapshot.getRef().setValue(1);

                }
                dataSnapshot.getRef().getParent().child(FB_REF_LIKES)
                        .child(firebaseAuth.getCurrentUser().getUid()).setValue((new Date()).getTime());
                listener.onSuccess();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        DatabaseReference reference1 = database.getReference("news/" + article.getArticleId());
        reference1.child(FB_REF_LIKES).child(firebaseAuth.getCurrentUser().getUid())
                .setValue((new Date()).getTime());
    */
        DatabaseReference users = database.getReference("users");
        users.child(firebaseAuth.getCurrentUser().getUid()).child("liked-news")
                .child(article.getArticleId()).setValue((new Date()).getTime());
    }

    public static void unlikeNews(News.Article article, final OnSuccessListener listener) {

        DatabaseReference reference = database.getReference("news/" + article.getArticleId());
        reference.child(FB_REF_LIKES_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int likesCount = dataSnapshot.getValue(int.class);
                    if (likesCount > 0) {
                        dataSnapshot.getRef().setValue(likesCount - 1);
                    }
                }

                listener.onSuccess();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child(FB_REF_LIKES).child(firebaseAuth.getCurrentUser().getUid()).removeValue();

        reference = FirebaseDatabase.getInstance()
                .getReference("users/" + firebaseAuth.getCurrentUser().getUid() + "/liked-news");
        reference.child(article.getArticleId()).removeValue();


    }

    public static void getLikesCount(String newsId, final OnResultListener listener) {
        DatabaseReference reference = database.getReference("news/" + newsId);
        reference.child(FB_REF_LIKES_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("likes-count", dataSnapshot.toString());
                if (dataSnapshot.getValue() != null) {
                    Log.d("likes-count", dataSnapshot.getValue().toString());
                    listener.onResult(dataSnapshot.getValue(long.class));
                } else {
                    listener.onResult(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void isLikedCurrentUser(String newsId, final OnResultListener listener) {
        DatabaseReference reference = database.getReference("news/" + newsId);
        reference.child(FB_REF_LIKES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                    listener.onResult(1);
                } else {
                    listener.onResult(-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static interface OnSuccessListener {
        void onSuccess();
    }

    public static interface OnResultListener {
        void onResult(long count);
    }


}
