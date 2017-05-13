package com.example.nikit.news.util.firebase;

import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.facebook.FacebookUser;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.util.facebook.LoadFacebookUserInfo;
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

import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_NEWS_OF_FRIENDS_ALL;
import static com.example.nikit.news.util.firebase.FirebaseConstants.FB_REF_USERS;

/**
 * Created by nikit on 16.04.2017.
 */

public class LoadNewsesFromFriends {

    private static final String TAG = LoadNewsesFromFriends.class.getSimpleName();
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    public LoadNewsesFromFriends() {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loadAll(final OnLoadingStateListener mListener) {
        if (firebaseAuth.getCurrentUser() == null) {
            return;
        }
        DatabaseReference reference = database.getReference(FB_REF_USERS + "/" + firebaseAuth.getCurrentUser().getUid() +
                "/" + FB_REF_NEWS_OF_FRIENDS_ALL);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    mListener.onFinish();
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

                    new FirebaseLoadNews().load(pair.getValue().keySet(),
                            new FirebaseLoadNews.OnProgressListener() {
                                @Override
                                public void onProgress(News.Article article) {
                                    final SharedNews sharedNews = pair.getValue().get(article.getArticleId());

                                    sharedNews.setArticle(article);
                                    new LoadFacebookUserInfo(new LoadFacebookUserInfo.OnCompleteListener() {
                                        @Override
                                        public void onComplete(FacebookUser facebookUser) {
                                            sharedNews.setFacebookUser(facebookUser);
                                            if (mListener != null) {
                                                mListener.onProgress(sharedNews);
                                                if (sharedNews.getNewsType().equals(SharedNews.NEWS_TYPE_NEW)) {
                                                    dataSnapshot.getRef().child(facebookUser.getId()).child(sharedNews.getNewsId())
                                                            .child("newsType").setValue(SharedNews.NEWS_TYPE_DEF);
                                                }
                                            }

                                        }
                                    }).load(pair.getKey());

                                }
                            });

                }
                mListener.onFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mListener.onFinish();
            }
        });
    }


    public interface OnLoadingStateListener {
        void onProgress(SharedNews sharedNews);

        void onFinish();
    }
}
