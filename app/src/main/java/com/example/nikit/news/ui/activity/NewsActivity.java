package com.example.nikit.news.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import com.example.nikit.news.R;

import com.example.nikit.news.services.NewsFromFriendsService;
import com.example.nikit.news.ui.SettingsActivity;
import com.example.nikit.news.ui.adapter.PagerAdapter;
import com.example.nikit.news.ui.dialog.FilterDialog;
import com.example.nikit.news.ui.fragment.FavoritesFragment;
import com.example.nikit.news.ui.fragment.NewsFromFriendsFragment;
import com.example.nikit.news.ui.fragment.NewsFragment;
import com.example.nikit.news.ui.fragment.TopNewsesFragment;
import com.example.nikit.news.util.Prefs;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class NewsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FilterDialog.NoticeDialogListener {

    public static final String FRAGMENT_TYPE_KEY = "fragment_type";
    public static final String SHARED_NEWS_FRAGMENT_TYPE = "shared_news_fragment";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Fragment fragment = ((PagerAdapter) viewPager.getAdapter()).getFragmentByPos(0);
                if (fragment instanceof NewsFragment) {
                    ((NewsFragment) fragment).updateContent();

                    Intent intent = new Intent(getApplicationContext(), NewsFromFriendsService.class);
                    if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {
                        startService(intent);
                    } else {
                        stopService(intent);
                    }
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //test
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
        pagerAdapter.addFragment(new NewsFragment(), "News");
        pagerAdapter.addFragment(TopNewsesFragment.newInstance(), "top");
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.nav_news_from_friends:
                Intent newsFromFriendsIntent = new Intent(this, AdditionalNewses.class);
                newsFromFriendsIntent.putExtra(AdditionalNewses.KEY_FRAGMENT_TYPE, AdditionalNewses.FRAG_TYPE_NEWS_FROM_FRIENDS);
                startActivity(newsFromFriendsIntent);
                break;

            case R.id.nav_favorites:
                Intent favoritesIntent = new Intent(this, AdditionalNewses.class);
                favoritesIntent.putExtra(AdditionalNewses.KEY_FRAGMENT_TYPE, AdditionalNewses.FRAG_TYPE_FAVORITE);
                startActivity(favoritesIntent);
                break;

            case R.id.nav_sign_out:
                signOut();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Fragment fragment = ((PagerAdapter) viewPager.getAdapter()).getFragmentByPos(0);
        if (fragment instanceof NewsFragment) {
            ((NewsFragment) fragment).updateContent();
        }
    }

    private void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (Prefs.getLoggedType() == Prefs.GOOGLE_LOGIN) {
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient);
                firebaseAuth.signOut();
                firebaseAuth.signOut();
                startActivity(intent);
            }

        } else if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            Prefs.setLoggedType(Prefs.NOT_LOGIN);
            firebaseAuth.signOut();
            startActivity(intent);
        }
    }

}
