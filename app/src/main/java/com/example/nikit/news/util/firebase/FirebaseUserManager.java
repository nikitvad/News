package com.example.nikit.news.util.firebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.facebook.FacebookUser;
import com.example.nikit.news.entities.firebase.AppUser;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.facebook.LoadFacebookFriends;
import com.example.nikit.news.util.facebook.LoadFacebookUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by nikit on 13.04.2017.
 */

public class FirebaseUserManager {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static void pushUserInfo() {
        final DatabaseReference refToCurrUser = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        final DatabaseReference refToOpenUid = firebaseDatabase.getReference("users/openUID");

        if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {

            new LoadFacebookUserInfo(new LoadFacebookUserInfo.OnCompleteListener() {
                @Override
                public void onComplete(FacebookUser facebookUser) {
                    refToOpenUid.child(facebookUser.getId()).setValue(firebaseAuth.getCurrentUser().getUid());
                }
            }).load();

            new LoadFacebookFriends(new LoadFacebookFriends.OnProgressListener() {
                @Override
                public void onProgress(final FacebookUser facebookUser) {
                    refToOpenUid.child(facebookUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String friendKey = dataSnapshot.getValue(String.class);
                            refToCurrUser.child("/friends/" + friendKey).setValue(facebookUser.toMap());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }).load();
        }

    }

    public static void pushUserInfo_v2(AppUser user) {
        final DatabaseReference refToUser = firebaseDatabase.getReference("users/" + user.getId() + "/userInfo");
        final DatabaseReference refToOpenUid = firebaseDatabase.getReference("users/openUID");

        refToUser.setValue(user.toHashMap());

        if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {

            new LoadFacebookUserInfo(new LoadFacebookUserInfo.OnCompleteListener() {
                @Override
                public void onComplete(FacebookUser facebookUser) {
                    refToOpenUid.child(facebookUser.getId()).setValue(firebaseAuth.getCurrentUser().getUid());
                }
            }).load();

            new LoadFacebookFriends(new LoadFacebookFriends.OnProgressListener() {
                @Override
                public void onProgress(final FacebookUser facebookUser) {
                    refToOpenUid.child(facebookUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String friendKey = dataSnapshot.getValue(String.class);
                            refToUser.child("/friends/" + friendKey).setValue(facebookUser.toMap());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }).load();
        }

    }

    public static void getCurrentUserInfo(final OnCompleteListener listener) {
        if(firebaseAuth.getCurrentUser()==null){
            return;
        }
        DatabaseReference reference = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid() + "/userInfo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    AppUser user = dataSnapshot.getValue(AppUser.class);
                    if (listener != null) {
                        listener.onComplete(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void synchronizeUserData(Context context) {
        final SqLiteDbHelper sqLiteDbHelper = new SqLiteDbHelper(context);
        final SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Long>> t = new GenericTypeIndicator<HashMap<String, Long>>() {
                };
                HashMap<String, Long> likedNewsIds;
                likedNewsIds = dataSnapshot.child("liked-news").getValue(t);
                sqLiteDbHelper.clearLikedNewsTable(database);
                if (likedNewsIds != null && likedNewsIds.size() > 0) {
                    sqLiteDbHelper.addAllLikedNewses(database, likedNewsIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface OnCompleteListener {
        void onComplete(AppUser user);
    }
}
