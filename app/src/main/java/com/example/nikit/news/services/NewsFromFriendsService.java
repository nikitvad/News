package com.example.nikit.news.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.nikit.news.R;
import com.example.nikit.news.util.NotificationUtils;
import com.example.nikit.news.util.firebase.FirebaseNewsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by nikit on 17.04.2017.
 */

public class NewsFromFriendsService extends IntentService {

    public boolean isDestroyed = false;

    public NewsFromFriendsService() {
        super(NewsFromFriendsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (!isDestroyed) {
            synchronized (this) {
                try {
                    wait(10000);
                    Log.d("sdfasdfs", "service is running");
                    FirebaseNewsManager.getCountNewSharedNewses(new FirebaseNewsManager.OnResultListener() {
                        @Override
                        public void onResult(long count) {
                            if (count > 0) {
                                NotificationUtils.getInstance(getApplicationContext()).createInfoNotification(
                                        getResources().getString(R.string.notification_there_newses_fom_friends));
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;

    }
}
