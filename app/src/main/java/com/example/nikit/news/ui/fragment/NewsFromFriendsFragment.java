package com.example.nikit.news.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.entities.ui.ListItem;
import com.example.nikit.news.ui.adapter.NewsesFromFriendsRvAdapter;
import com.example.nikit.news.util.firebase.LoadNewsesFromFriends;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewsFromFriendsFragment extends Fragment {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private NewsesFromFriendsRvAdapter adapter;
    private ArrayList<ListItem> items = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public NewsFromFriendsFragment() {
        // Required empty public constructor
    }

    public static NewsFromFriendsFragment newInstance() {
        NewsFromFriendsFragment fragment = new NewsFromFriendsFragment();
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
        adapter = new NewsesFromFriendsRvAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_news_of_friends);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onResume() {
        super.onResume();

        updateContent();

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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateContent() {

        adapter.clearData();
        LoadNewsesFromFriends loadNewsesFromFriends = new LoadNewsesFromFriends();
        loadNewsesFromFriends.loadAll(new LoadNewsesFromFriends.OnSharedNewsProgressListener() {
            @Override
            public void onProgress(SharedNews sharedNews) {
                adapter.addShareNews(sharedNews);
                Log.d("sdfgsdfsdfg", sharedNews.toString());
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
