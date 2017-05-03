package com.example.nikit.news.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.NewsRvAdapter;
import com.example.nikit.news.util.firebase.FirebaseLoadNews;
import com.example.nikit.news.util.firebase.LoadNewsesFromFriends;


public class FavoritesFragment extends Fragment {
    private RecyclerView rvFavoriteNews;
    private NewsRvAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavoriteNews = (RecyclerView) view.findViewById(R.id.rv_favorite_news);
        adapter = new NewsRvAdapter(this);
        rvFavoriteNews.setAdapter(adapter);
        rvFavoriteNews.setLayoutManager(new LinearLayoutManager(getContext()));

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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void updateContent() {
        adapter.clearData();
        swipeRefreshLayout.setRefreshing(true);
        new FirebaseLoadNews().loadFavorites(new FirebaseLoadNews.OnFavoriteNewsProgressListener() {
            @Override
            public void onProgress(News.Article article) {
                adapter.addArticle(article);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
