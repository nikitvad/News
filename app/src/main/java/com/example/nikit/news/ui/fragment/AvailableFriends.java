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
import com.example.nikit.news.entities.facebook.User;
import com.example.nikit.news.ui.adapter.FriendsRvAdapter;
import com.example.nikit.news.util.facebook.LoadUserFriends;

import java.util.HashSet;

public class AvailableFriends extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HashSet<String> selectedFriendUids;

    private RecyclerView rvFriends;
    private Button btNext;
    private FriendsRvAdapter friendsRvAdapter;
    private OnFragmentInteractionListener fragmentInteractionListener;

    public AvailableFriends() {
        selectedFriendUids = new HashSet<>();
    }

    // TODO: Rename and change types and number of parameters
    public static AvailableFriends newInstance(String param1, String param2) {
        AvailableFriends fragment = new AvailableFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        btNext = (Button) view.findViewById(R.id.bt_asdfasdfasdf);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentInteractionListener!=null){
                    if(selectedFriendUids.size()>0) {
                        fragmentInteractionListener.onFragmentInteraction(selectedFriendUids);
                    }else{
                        Toast.makeText(getContext(), "need to select minimum one friend", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Toast.makeText(getContext(), "sdfsdfgsdfg", Toast.LENGTH_SHORT).show();
        rvFriends = (RecyclerView) view.findViewById(R.id.rv_friends);
        friendsRvAdapter = new FriendsRvAdapter();
        friendsRvAdapter.setOnClickListener(new FriendsRvAdapter.OnClickListener() {
            @Override
            public void onClick(View v, User user) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.cb_friend_selected);
                if (cb.isChecked()) {
                    selectedFriendUids.add(user.getId());
                } else {
                    selectedFriendUids.remove(user.getId());
                }
            }
        });


        rvFriends.setAdapter(friendsRvAdapter);
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        new LoadUserFriends(new LoadUserFriends.OnProgressListener() {
            @Override
            public void onProgress(User user) {
                friendsRvAdapter.addFriend(user);
            }
        }).load();




    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(HashSet<String> FriedUids);
    }

}
