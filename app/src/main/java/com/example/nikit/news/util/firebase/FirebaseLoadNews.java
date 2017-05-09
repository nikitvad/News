package com.example.nikit.news.util.firebase;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nikit.news.entities.News;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Set;

import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKED_NEWS;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_LIKES_COUNT;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_NEWS;

/**
 * Created by nikit on 16.04.2017.
 */

public class FirebaseLoadNews {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public static void loadTopNewses(final OnProgressListener listener) {
        DatabaseReference reference = database.getReference(FB_REF_NEWS);

        Query topNewses = reference.orderByChild(FB_REF_LIKES_COUNT).limitToLast(20);

        topNewses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("top_newses", dataSnapshot.toString());

                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<HashMap<String, News.Article>> t = new GenericTypeIndicator<HashMap<String, News.Article>>() {
                    };

                    HashMap<String, News.Article> hashMap = dataSnapshot.getValue(t);
                    Log.d("top_newses", hashMap.toString());

                    for (final News.Article item : hashMap.values()) {
                        FirebaseNewsManager.isLikedCurrentUser(item.getArticleId(), new FirebaseNewsManager.OnResultListener() {
                            @Override
                            public void onResult(long count) {
                                item.setLiked(count > 0 ? true : false);
                                listener.onProgress(item);
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

    public static void load(Set<String> newsIds, @Nullable final OnProgressListener mListener) {
        DatabaseReference reference = database.getReference("news");

        for (String newsId : newsIds) {
            reference.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    News.Article article = dataSnapshot.getValue(News.Article.class);

                    if (dataSnapshot.child(FB_REF_LIKES).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        article.setLiked(true);
                    } else {
                        article.setLiked(false);
                    }
                    mListener.onProgress(article);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static void loadFavorites(final OnFavoriteNewsLoadingStateListener listener) {
        DatabaseReference reference = database.getReference("users/" + firebaseAuth.getCurrentUser().getUid());

        reference.child(FB_REF_LIKED_NEWS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Log.d("loadFavorites", dataSnapshot.toString());
                    GenericTypeIndicator<HashMap<String, Long>> t = new GenericTypeIndicator<HashMap<String, Long>>() {
                    };
                    HashMap<String, Long> hashMap = dataSnapshot.getValue(t);
                    Log.d("loadFavorites", hashMap.toString());


                    new FirebaseLoadNews().load(hashMap.keySet(), new FirebaseLoadNews.OnProgressListener() {
                        @Override
                        public void onProgress(News.Article article) {
                            if (listener != null) {
                                listener.onProgress(article);
                                Log.d("loadFavorites", article.toString());
                            }
                        }
                    });
                }
                listener.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFinish();
            }
        });

    }

    public interface OnFavoriteNewsLoadingStateListener {
        void onProgress(News.Article article);
        void onFinish();

    }

    public interface OnProgressListener {
        void onProgress(News.Article article);
    }
}
