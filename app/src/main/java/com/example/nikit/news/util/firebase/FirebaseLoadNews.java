package com.example.nikit.news.util.firebase;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nikit.news.entities.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by nikit on 16.04.2017.
 */

public class FirebaseLoadNews {
    private OnProgressListener mListener;

    public FirebaseLoadNews(@Nullable OnProgressListener mListener) {
        this.mListener = mListener;
    }

    public void load(Set<String> newsIds) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("news");

        for (String newsId : newsIds) {
            reference.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    News.Article article = dataSnapshot.getValue(News.Article.class);
                    Log.d("getNewsesOfFriendsss", dataSnapshot.toString());
                    if (mListener != null && article!=null) {
                        mListener.onProgress(article);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    public interface OnProgressListener {
        void onProgress(News.Article article);
    }
}
