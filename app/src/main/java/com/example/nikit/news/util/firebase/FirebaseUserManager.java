package com.example.nikit.news.util.firebase;

import com.example.nikit.news.entities.Friend_temp_name;
import com.example.nikit.news.entities.facebook.User;
import com.example.nikit.news.util.facebook.LoadUserFriends;
import com.example.nikit.news.util.facebook.LoadUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nikit on 13.04.2017.
 */

public class FirebaseUserManager {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static void pushUserFriends(String publicUID, ArrayList<Friend_temp_name> friends) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = firebaseDatabase.getReference("users/openUID");
        reference.child(publicUID).setValue(user.getUid());

        if (friends.size() > 0) {
            reference = firebaseDatabase.getReference("users/" + user.getUid());
            HashMap<String, String> idsMap = new HashMap<>();

            for (Friend_temp_name item : friends) {
                idsMap.put(item.getId() + "", "true");
            }

            reference.child("friends").setValue(idsMap);
        }
    }

    public static void pushUserFriendV2(String currUserOpenKey, ArrayList<User> friends) {
        final DatabaseReference refToUser = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        DatabaseReference refToOpenUid = firebaseDatabase.getReference("users/openUID");

        refToOpenUid.child(currUserOpenKey).setValue(firebaseAuth.getCurrentUser().getUid());

        for (final User user : friends) {
            refToOpenUid.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String friendsUserKey = dataSnapshot.getValue(String.class);

                    refToUser.child("/friends/" + friendsUserKey).setValue(user.toMap());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static void pushUserFriendV2(final User friends) {
        final DatabaseReference refToUser = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        DatabaseReference refToOpenUid = firebaseDatabase.getReference("users/openUID");

        refToOpenUid.child(friends.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friendsUserKey = dataSnapshot.getValue(String.class);

                refToUser.child("/friends/" + friendsUserKey).setValue(friends.toMap());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static void dsfgsdfgsdfg(){
        final DatabaseReference refToCurrUser = firebaseDatabase.getReference("users/" + firebaseAuth.getCurrentUser().getUid());
        final DatabaseReference refToOpenUid = firebaseDatabase.getReference("users/openUID");

        new LoadUserInfo(new LoadUserInfo.OnCompleteListener() {
            @Override
            public void onComplete(User user) {
                refToOpenUid.child(user.getId()).setValue(firebaseAuth.getCurrentUser().getUid());
            }
        }).load();

        new LoadUserFriends(new LoadUserFriends.OnProgressListener() {
            @Override
            public void onProgress(final User user) {
                refToOpenUid.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String friendKey = dataSnapshot.getValue(String.class);
                        refToCurrUser.child("/friends/"+friendKey).setValue(user.toMap());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }).load();

    }


}
