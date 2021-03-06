package com.example.nikit.news.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.ui.adapter.NewsesFromFriendsRvAdapter;
import com.example.nikit.news.util.NetworkUtil;
import com.example.nikit.news.util.firebase.LoadNewsesFromFriends;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsFromFriendsFragment extends Fragment {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private NewsesFromFriendsRvAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoNewses;

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

        tvNoNewses = (TextView) view.findViewById(R.id.tv_news_from_friends_no_newses);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateContent();
            }
        });

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
        return inflater.inflate(R.layout.fragment_news_from_friends, container, false);
    }

    public void updateContent() {

        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            swipeRefreshLayout.setRefreshing(true);
            adapter.clearData();
            tvNoNewses.setVisibility(View.GONE);
            LoadNewsesFromFriends loadNewsesFromFriends = new LoadNewsesFromFriends();
            loadNewsesFromFriends.loadAll(new LoadNewsesFromFriends.OnLoadingStateListener() {
                @Override
                public void onProgress(SharedNews sharedNews) {
                    adapter.addShareNews(sharedNews);
                }

                @Override
                public void onFinish() {
                    swipeRefreshLayout.setRefreshing(false);
                    if (adapter.getItemCount() < 1) {
                        tvNoNewses.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            tvNoNewses.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
        }
    }

}
