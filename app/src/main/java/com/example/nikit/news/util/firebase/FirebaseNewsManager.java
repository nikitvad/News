package com.example.nikit.news.util.firebase;

import android.util.Log;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Date;

import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKED_NEWS;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES_COUNT;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_NEWS;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_NEWS_OF_FRIENDS_ALL;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_NEWS_OF_FRIENDS_NEW;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_OPEN_UID;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_USERS;

/**
 * Created by nikit on 11.04.2017.
 */

public class FirebaseNewsManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static void pushNews(News.Article article) {
        DatabaseReference reference = database.getReference(FB_REF_NEWS);
        reference.child(article.getArticleId()).setValue(article.toMap());
    }

    public static void shareNewsWithFriend(final String authorUid, final String openUid, final SharedNews news) {
        DatabaseReference reference = database.getReference(FB_REF_OPEN_UID + "/" + openUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue().toString();

                DatabaseReference ref = database.getReference(FB_REF_USERS + "/" + uid + "/" + FB_REF_NEWS_OF_FRIENDS_ALL);
                ref = ref.child(authorUid).child(news.getNewsId());
                ref.setValue(news.toMap());

                ref = database.getReference(FB_REF_USERS + "/" + uid + "/" + FB_REF_NEWS_OF_FRIENDS_NEW);
                ref.child(news.getNewsId()).setValue(authorUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getCountNewSharedNewses(final OnResultListener listener) {
        DatabaseReference reference = database.getReference(FB_REF_USERS + "/" + firebaseAuth.getCurrentUser().getUid());
        reference.child(FB_REF_NEWS_OF_FRIENDS_NEW).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long count = dataSnapshot.getChildrenCount();
                    if (listener != null) {
                        listener.onResult(count);
                        Log.d("sdfasdfs", count + "");
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

        DatabaseReference reference = database.getReference(FB_REF_NEWS + "/" + article.getArticleId());
        reference.child(FB_REF_LIKES_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

        DatabaseReference users = database.getReference(FB_REF_USERS);
        users.child(firebaseAuth.getCurrentUser().getUid()).child(FB_REF_LIKED_NEWS)
                .child(article.getArticleId()).setValue((new Date()).getTime());
    }

    public static void unlikeNews(News.Article article, final OnSuccessListener listener) {

        DatabaseReference reference = database.getReference(FB_REF_NEWS + "/" + article.getArticleId());
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
                .getReference(FB_REF_USERS + "/" + firebaseAuth.getCurrentUser().getUid() + "/" + FB_REF_LIKED_NEWS);
        reference.child(article.getArticleId()).removeValue();


    }

    public static void getLikesCount(String newsId, final OnResultListener listener) {
        DatabaseReference reference = database.getReference(FB_REF_NEWS + "/" + newsId);
        reference.child(FB_REF_LIKES_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
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
        DatabaseReference reference = database.getReference(FB_REF_NEWS + "/" + newsId);
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
