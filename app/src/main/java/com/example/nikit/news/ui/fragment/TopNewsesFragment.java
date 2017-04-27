package com.example.nikit.news.ui.fragment;


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
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.NewsRvAdapter;
import com.example.nikit.news.util.firebase.FirebaseLoadNews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopNewsesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopNewsesFragment extends Fragment {

    private RecyclerView rvTopNewses;
    private NewsRvAdapter adapter;

    public TopNewsesFragment() {
        // Required empty public constructor
    }

    public static TopNewsesFragment newInstance() {
        TopNewsesFragment fragment = new TopNewsesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_newses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new NewsRvAdapter(this);
        rvTopNewses = (RecyclerView) view.findViewById(R.id.rv_top_newses);
        rvTopNewses.setAdapter(adapter);
        rvTopNewses.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseLoadNews.loadTopNewses(new FirebaseLoadNews.OnProgressListener() {
            @Override
            public void onProgress(News.Article article) {
                adapter.addArticle(article);
            }
        });
    }


}
