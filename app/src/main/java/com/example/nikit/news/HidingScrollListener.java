package com.example.nikit.news;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

/**
 * Created by nikit on 15.03.2017.
 */

public class HidingScrollListener extends RecyclerView.OnScrollListener {
    // Keeps track of the overall vertical offset in the list
    int verticalOffset;
    boolean scrollingUp;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private Button btFilter;

    public HidingScrollListener(Toolbar toolbar, @Nullable TabLayout tabLayout, @Nullable Button button) {
        this.toolbar = toolbar;
        this.tabLayout = tabLayout;
        this.btFilter = button;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (scrollingUp) {
                if (verticalOffset > toolbar.getHeight()) {
                    toolbarAnimateHide();
                } else {
                    toolbarAnimateShow(verticalOffset);
                }
            } else {
                if (toolbar.getTranslationY() < toolbar.getHeight() * -0.6 && verticalOffset > toolbar.getHeight()) {
                    toolbarAnimateHide();
                } else {
                    toolbarAnimateShow(verticalOffset);
                }
            }
        }
    }


    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        verticalOffset += dy;
        scrollingUp = dy > 0;
        int toolbarYOffset = (int) (dy - toolbar.getTranslationY());
        toolbar.animate().cancel();
        if (btFilter != null) btFilter.animate().cancel();
        if (tabLayout != null) tabLayout.animate().cancel();
        if (scrollingUp) {
            if (toolbarYOffset < toolbar.getHeight()) {
                setTranslationY(-toolbarYOffset);
            } else {
                setTranslationY(-toolbar.getHeight());
            }
        } else {
            if (toolbarYOffset < 0) {
                setTranslationY(0);
            } else {
                setTranslationY(-toolbarYOffset);
            }
        }

    }


    private void setTranslationY(int verticalOffset) {
        toolbar.setTranslationY(verticalOffset);
        if (tabLayout != null) tabLayout.setTranslationY(verticalOffset);
        if (btFilter != null) btFilter.setTranslationY(-verticalOffset);

    }

    private void toolbarAnimateShow(final int verticalOffset) {
        toolbar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);

        if (btFilter != null) {
            btFilter.animate()
                    .translationY(0)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(180);
        }

        if (tabLayout != null) {
            tabLayout.animate()
                    .translationY(0)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(180);
        }


    }

    private void toolbarAnimateHide() {
        toolbar.animate()
                .translationY(-toolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);

        if (btFilter != null) {
            btFilter.animate()
                    .translationY(btFilter.getHeight())
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(180);
        }

        tabLayout.animate()
                .translationY(-toolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);

    }
}
