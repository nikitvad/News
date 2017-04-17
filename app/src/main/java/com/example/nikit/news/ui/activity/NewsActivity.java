package com.example.nikit.news.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import com.example.nikit.news.R;

import com.example.nikit.news.ui.adapter.PagerAdapter;
import com.example.nikit.news.ui.dialog.FilterDialog;
import com.example.nikit.news.ui.fragment.NewsOfFriendsFragment;
import com.example.nikit.news.ui.fragment.RetrofitFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.auth.FirebaseAuth;

public class NewsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FilterDialog.NoticeDialogListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    private ObservableScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Fragment fragment = ((PagerAdapter) viewPager.getAdapter()).getFragmentByPos(0);
                if (fragment instanceof RetrofitFragment) {
                    ((RetrofitFragment) fragment).updateContent();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //new LoadSourceIdsAsync().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new RetrofitFragment(), "News");
        pagerAdapter.addFragment(NewsOfFriendsFragment.newInstance("", ""), "friends");
        //pagerAdapter.addFragment(new SourcesFragment(), "Sources");
        viewPager.setAdapter(pagerAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Fragment fragment = ((PagerAdapter) viewPager.getAdapter()).getFragmentByPos(0);
        if (fragment instanceof RetrofitFragment) {
            ((RetrofitFragment) fragment).updateContent();
        }
    }

}
