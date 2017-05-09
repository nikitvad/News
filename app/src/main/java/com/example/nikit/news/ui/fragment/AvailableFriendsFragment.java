package com.example.nikit.news.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.facebook.FacebookUser;
import com.example.nikit.news.ui.adapter.FriendsRvAdapter;
import com.example.nikit.news.util.facebook.LoadFacebookFriends;

import java.util.HashSet;

public class AvailableFriendsFragment extends Fragment {


    private HashSet<String> selectedFriendUids;

    private RecyclerView rvFriends;
    private Button btNext;
    private FriendsRvAdapter friendsRvAdapter;
    private OnFragmentInteractionListener fragmentInteractionListener;

    public AvailableFriendsFragment() {
        selectedFriendUids = new HashSet<>();
    }

    // TODO: Rename and change types and number of parameters
    public static AvailableFriendsFragment newInstance() {
        AvailableFriendsFragment fragment = new AvailableFriendsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCommentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btNext = (Button) view.findViewById(R.id.bt_share_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentInteractionListener!=null){
                    if(selectedFriendUids.size()>0) {
                        fragmentInteractionListener.onFragmentInteraction(selectedFriendUids);
                    }else{
                        Toast.makeText(getContext(), "Need to select minimum one friend", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rvFriends = (RecyclerView) view.findViewById(R.id.rv_friends);
        friendsRvAdapter = new FriendsRvAdapter();
        friendsRvAdapter.setOnClickListener(new FriendsRvAdapter.OnClickListener() {
            @Override
            public void onClick(View v, FacebookUser facebookUser) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.cb_friend_selected);
                if (cb.isChecked()) {
                    selectedFriendUids.add(facebookUser.getId());
                } else {
                    selectedFriendUids.remove(facebookUser.getId());
                }
            }
        });


        rvFriends.setAdapter(friendsRvAdapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        new LoadFacebookFriends(new LoadFacebookFriends.OnProgressListener() {
            @Override
            public void onProgress(FacebookUser facebookUser) {
                friendsRvAdapter.addFriend(facebookUser);
            }
        }).load();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(HashSet<String> FriedUids);
    }

}
