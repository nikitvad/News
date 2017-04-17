package com.example.nikit.news.util;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by nikit on 15.03.2017.
 */

public class ViewUtil {

    public static void hideToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public static void showToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
