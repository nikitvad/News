package com.example.nikit.news.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.util.NetworkUtil;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.firebase.FirebaseUserManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by nikit on 22.03.2017.
 */

public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseManager.initializeInstance(new SqLiteDbHelper(getApplicationContext()));

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);

        Prefs.init(getApplicationContext());
        Prefs.setDisplayWidth(size.x);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUserManager.synchronizeUserData(getApplicationContext());
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
