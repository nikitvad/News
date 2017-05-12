package com.example.nikit.news.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Prefs {

    // user
    public static final String USER_ID = "prefs_user_id";
    public static final String LOGGED_TYPE = "prefs_logged_type";
    public static final String SOURCES_COUNT = "prefs_sources_count";
    public static final String FIRST_LAUNCH = "first_launch";
    public static final String SOURCES_FILTER = "sources-filter";
    public static final String DISPLAY_WIDTH = "display_width";

    public static final int NOT_LOGIN = 0;
    public static final int GOOGLE_LOGIN = 1;
    public static final int FACEBOOK_LOGIN = 2;
    public static final int EMAIL_LOGIN = 3;

    private static SharedPreferences sPrefs;

    private Prefs() {
    }

    public static void init(Context context) {
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static int getLoggedType() {
        return getInt(LOGGED_TYPE);
    }

    public static void setLoggedType(int loggedType) {
        setInt(LOGGED_TYPE, loggedType);
    }

    public static String getUserId() {
        return getString(USER_ID);
    }

    public static void setUserId(String userId) {
        setString(USER_ID, userId);
    }

    public static boolean isFirstLaunch(){
        return !getBool(FIRST_LAUNCH);
    }

    public static void setFirstLaunch(boolean value){
        setBool(FIRST_LAUNCH, !value);
    }

    public static void setSourcesFilter(Set<String> sources){
        setStringSet(SOURCES_FILTER, sources);
    }

    public static int getDisplayWidth(){
        return sPrefs.getInt(DISPLAY_WIDTH, 768);
    }

    public static void setDisplayWidth(int width){
        setInt(DISPLAY_WIDTH, width);
    }

    public static Set<String> getSourcesFilter(){
        return sPrefs.getStringSet(SOURCES_FILTER, new HashSet<String>());
    }

    private static String getString(String key) {
        return sPrefs.getString(key, "");
    }

    private static void setString(String key, String value) {
        sPrefs.edit().putString(key, value).apply();
    }

    private static int getInt(String key) {
        return sPrefs.getInt(key, 0);
    }

    private static void setBool(String key, boolean value){
        sPrefs.edit().putBoolean(key, value).apply();
    }

    public static boolean getBool(String key){
       return sPrefs.getBoolean(key, false);
    }
    private static void setInt(String key, int value) {
        sPrefs.edit().putInt(key, value).apply();
    }

    private static Set<String> getStringSet(String key) {
        return sPrefs.getStringSet(key, null);
    }

    private static void setStringSet(String key, Set<String> stringSet) {
        sPrefs.edit().remove(key);
        sPrefs.edit().putStringSet(key, stringSet).apply();
    }

    public static int getSourcesCount() {
        return getInt(SOURCES_COUNT);
    }

    public static void setSourcesCount(int count) {
        setInt(SOURCES_COUNT, count);
    }

}