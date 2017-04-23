package com.example.nikit.news.services;

import android.app.IntentService;
import android.content.Intent;

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

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    private long newsCount = 0;

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */

    public NewsFromFriendsService() {
        super(NewsFromFriendsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (true) {
            synchronized (this) {
                try {
                    wait(10000);
                    FirebaseNewsManager.getCountNewSharedNewses(new FirebaseNewsManager.OnResultListener() {
                        @Override
                        public void onResult(long count) {
                            if (count > 0) {
                                if (count - newsCount > 0) {
                                    newsCount = count;
                                    NotificationUtils.getInstance(getApplicationContext()).createInfoNotification("Драсте");
                                }
                            } else {
                                newsCount = 0;
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        }

    }


}
