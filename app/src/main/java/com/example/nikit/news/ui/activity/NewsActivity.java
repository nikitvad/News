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
import android.widget.Toast;

import com.example.nikit.news.R;

import com.example.nikit.news.services.NewsFromFriendsService;
import com.example.nikit.news.ui.adapter.PagerAdapter;
import com.example.nikit.news.ui.dialog.FilterDialog;
import com.example.nikit.news.ui.fragment.NewsFromFriendsFragment;
import com.example.nikit.news.ui.fragment.RetrofitFragment;
import com.example.nikit.news.util.Prefs;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.GoogleAuthUtil;
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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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
                if (fragment instanceof RetrofitFragment) {
                    ((RetrofitFragment) fragment).updateContent();

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
        pagerAdapter.addFragment(NewsFromFriendsFragment.newInstance(), "friends");
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_sign_out) {
            signOut();
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

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if (intent.getExtras() != null && intent.getExtras().containsKey(FRAGMENT_TYPE_KEY)) {
            String fragmentType = intent.getExtras().getString(FRAGMENT_TYPE_KEY);
            if (fragmentType.equals(SHARED_NEWS_FRAGMENT_TYPE)) {
                viewPager.setCurrentItem(1);
                intent.removeExtra(FRAGMENT_TYPE_KEY);
            }
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
