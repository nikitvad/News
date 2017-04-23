package com.example.nikit.news.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.nikit.news.R;
import com.example.nikit.news.ui.activity.NewsActivity;

import java.util.HashMap;

/**
 * Created by nikit on 17.04.2017.
 */

public class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();

    private static NotificationUtils instance;

    private static Context context;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, Notification> notifications;

    private NotificationUtils(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
    }

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtils(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    public int createInfoNotification(String message){
        Intent notificationIntent = new Intent(context, NewsActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(NewsActivity.FRAGMENT_TYPE_KEY, NewsActivity.SHARED_NEWS_FRAGMENT_TYPE);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
//NotificationCompat.Builder nb = new NotificationBuilder(context) //для версии Android > 3.0
                .setSmallIcon(R.drawable.com_facebook_button_icon)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("AppName")
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = nb.getNotification();
        manager.notify(lastId, notification);
        notifications.put(lastId, notification);
        return lastId++;
    }


    public interface OnEventListener{
        void onEvent();
    }

}
