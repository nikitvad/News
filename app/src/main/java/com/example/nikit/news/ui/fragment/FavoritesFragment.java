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
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.NewsRvAdapter;
import com.example.nikit.news.util.NetworkUtil;
import com.example.nikit.news.util.firebase.FirebaseLoadNews;


public class FavoritesFragment extends Fragment {
    private RecyclerView rvFavoriteNews;
    private NewsRvAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoFavorites;

    public FavoritesFragment() {
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

        tvNoFavorites = (TextView) view.findViewById(R.id.tv_no_favorites);

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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void updateContent() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            adapter.clearData();
            swipeRefreshLayout.setRefreshing(true);
            tvNoFavorites.setVisibility(View.GONE);
            new FirebaseLoadNews().loadFavorites(new FirebaseLoadNews.OnFavoriteNewsLoadingStateListener() {
                @Override
                public void onProgress(News.Article article) {
                    adapter.addArticle(article);
                }

                @Override
                public void onFinish() {
                    swipeRefreshLayout.setRefreshing(false);
                    if (adapter.getArticles().size() < 1) {
                        tvNoFavorites.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            tvNoFavorites.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
        }
    }

}
