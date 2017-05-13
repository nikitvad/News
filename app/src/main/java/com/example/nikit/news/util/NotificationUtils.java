package com.example.nikit.news.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.nikit.news.Constants;
import com.example.nikit.news.R;
import com.example.nikit.news.ui.activity.AdditionalNewses;

import java.util.HashMap;

/**
 * Created by nikit on 17.04.2017.
 */

public class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();

    public static SharedPreferences sharedPreferences;

    private static NotificationUtils instance;

    private static Context context;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, Notification> notifications;

    private NotificationUtils(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtils(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    public int createInfoNotification(String message) {
        Intent notificationIntent = new Intent(context, AdditionalNewses.class);
        notificationIntent.putExtra(AdditionalNewses.KEY_FRAGMENT_TYPE, AdditionalNewses.FRAG_TYPE_NEWS_FROM_FRIENDS);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.com_facebook_button_icon)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("News");

        if (sharedPreferences.getBoolean(Constants.PREF_KEY_NOTIFICATIONS_SOUND, false)) {
            if (sharedPreferences.getBoolean(Constants.PREF_KEY_NOTIFICATIONS_VIBRATE, false)) {
                nb.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            } else {
                nb.setDefaults(Notification.DEFAULT_SOUND);
            }
        } else if (sharedPreferences.getBoolean(Constants.PREF_KEY_NOTIFICATIONS_VIBRATE, false)) {
            nb.setDefaults(Notification.DEFAULT_VIBRATE);
        }


        Notification notification = nb.getNotification();
        manager.notify(lastId, notification);
        notifications.put(lastId, notification);
        return lastId++;
    }

}
