package com.example.nikit.news.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.entities.ui.RecycleListItem;
import com.example.nikit.news.ui.adapter.SharedNewsRvAdapter;
import com.example.nikit.news.util.firebase.LoadNewsesOfFriends;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewsOfFriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private SharedNewsRvAdapter adapter;
    private ArrayList<RecycleListItem> articles = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public NewsOfFriendsFragment() {
        // Required empty public constructor
    }

    public static NewsOfFriendsFragment newInstance(String param1, String param2) {
        NewsOfFriendsFragment fragment = new NewsOfFriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference("news");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SharedNewsRvAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_news_of_friends);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new LoadNewsesOfFriends(new LoadNewsesOfFriends.OnProgressListener() {
            @Override
            public void onProgress(SharedNews sharedNews) {
                adapter.addShareNews(sharedNews);
            }
        }).getNewsesOfFriends();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_of_friends, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnCommentFragmentInteractionListener) {
            mListener = (OnCommentFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCommentFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
