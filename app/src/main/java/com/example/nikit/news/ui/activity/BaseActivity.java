package com.example.nikit.news.ui.activity;

import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.nikit.news.R;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.UpdateAvailableSourcesAsync;


import java.util.HashSet;

import static com.example.nikit.news.ui.fragment.SettingsFragment.KEY_PREF_SYNC_FREQUENCY;

/**
 * Created by nikit on 26.03.2017.
 */

public class BaseActivity extends AppCompatActivity
        implements UpdateAvailableSourcesAsync.OnUpdateStageListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //SharedPreferences sharedPreferences = getSharedPreferences(getApplicationContext().getPackageName(),
        //        Context.MODE_PRIVATE);

        if (Prefs.isFirstLaunch() || Prefs.getLoggedType() == Prefs.NOT_LOGIN) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            HashSet<String> strings = new HashSet<>();
            strings.add("bbc-news");
            Prefs.setSourcesFilter(strings);
            new UpdateAvailableSourcesAsync(getApplicationContext(), this).execute();

            PreferenceManager.setDefaultValues(this, R.xml.preference_fragment, false);

            Prefs.setFirstLaunch(false);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());




        }
        if (Prefs.getSourcesCount() == 0) {
            new UpdateAvailableSourcesAsync(getApplicationContext(), this).execute();
        }
    }


    @Override
    public void onUpdateSuccess(int sourceCount) {
        Prefs.setSourcesCount(sourceCount);
    }

    @Override
    public void onUpdateFail() {

    }

}
