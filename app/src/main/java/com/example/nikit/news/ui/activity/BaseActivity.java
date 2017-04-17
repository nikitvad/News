package com.example.nikit.news.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.nikit.news.Constants;

import com.example.nikit.news.R;
import com.example.nikit.news.ui.activity.LoginActivity;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.UpdateAvailableSourcesAsync;


import java.util.HashSet;

/**
 * Created by nikit on 26.03.2017.
 */

public class BaseActivity extends AppCompatActivity
        implements UpdateAvailableSourcesAsync.OnUpdateStageListener {
    private static final String FIRST_LAUNCH = "first_launch";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(getApplicationContext().getPackageName(),
                Context.MODE_PRIVATE);
        if (Prefs.isFirstLaunch()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            HashSet<String> strings = new HashSet<>();
            strings.add("bbc-news");
            sharedPreferences.edit().putStringSet(Constants.FILTER_SOURCES_TAG, strings).commit();
            new UpdateAvailableSourcesAsync(getApplicationContext(), this).execute();

            Prefs.setFirstLaunch(false);
        }
        if(Prefs.getSourcesCount()==0){
            new UpdateAvailableSourcesAsync(getApplicationContext(), this).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_login: {
                Log.d("onOptionsItemSelected", "start");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        }
        return true;
    }

    @Override
    public void onUpdateSuccess(int sourceCount) {
        Prefs.setSourcesCount(sourceCount);
    }

    @Override
    public void onUpdateFail() {

    }
}
