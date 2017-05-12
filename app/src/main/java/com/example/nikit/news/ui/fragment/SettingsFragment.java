package com.example.nikit.news.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.util.Log;

import com.example.nikit.news.Constants;
import com.example.nikit.news.R;
import com.example.nikit.news.services.NewsFromFriendsService;
import com.example.nikit.news.util.Prefs;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String KEY_PREF_SYNC_FREQUENCY = "pref_key_sync_frequency";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Constants.PREF_KEY_RECEIVE_NOTIFICATIONS)) {
            Intent intent = new Intent(getActivity().getApplicationContext(), NewsFromFriendsService.class);
            if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {
                if (sharedPreferences.getBoolean(Constants.PREF_KEY_RECEIVE_NOTIFICATIONS, false)) {
                    getActivity().startService(intent);
                } else {
                    getActivity().stopService(intent);
                }
            }
        }
    }
}

