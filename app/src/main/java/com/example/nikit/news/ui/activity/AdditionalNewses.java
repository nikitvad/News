package com.example.nikit.news.ui.activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.nikit.news.R;
import com.example.nikit.news.ui.fragment.FavoritesFragment;
import com.example.nikit.news.ui.fragment.NewsFromFriendsFragment;

public class AdditionalNewses extends AppCompatActivity {
    public static final String TAG = AdditionalNewses.class.getSimpleName();

    public static final String KEY_FRAGMENT_TYPE = "fragment_type";
    public static final int FRAG_TYPE_NEWS_FROM_FRIENDS = 1;
    public static final int FRAG_TYPE_FAVORITE = 2;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_newses);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(KEY_FRAGMENT_TYPE)) {
            int fragmentType = bundle.getInt(KEY_FRAGMENT_TYPE);

            if (fragmentType == FRAG_TYPE_NEWS_FROM_FRIENDS) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_fragment_container, NewsFromFriendsFragment.newInstance()).commit();

            } else if (fragmentType == FRAG_TYPE_FAVORITE) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_fragment_container, FavoritesFragment.newInstance()).commit();
            }
        } else {
            Log.e(TAG, "onCreate: wrong fragment type");
            finish();
        }
    }
}
