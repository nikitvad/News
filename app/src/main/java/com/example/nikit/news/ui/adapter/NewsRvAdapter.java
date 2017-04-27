package com.example.nikit.news.ui.adapter;

import android.content.res.Resources;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.viewHolder.ArticleViewHolder;

import java.util.ArrayList;

/**
 * Created by nikit on 14.03.2017.
 */

public class NewsRvAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private final ArrayList<News.Article> articles;
    private Fragment fragment;

    private int imageHeight = 720;
    private int imageWidth = 480;

    public NewsRvAdapter(Fragment fragment) {
        articles = new ArrayList<>();
        this.fragment = fragment;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        DatabaseManager.getInstance().closeDatabase();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Resources r = recyclerView.getResources();

        imageWidth = r.getDisplayMetrics().widthPixels;
        imageHeight = (int)(imageWidth/1.77);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArticleViewHolder(inflater.inflate(R.layout.article_item, parent, false), this, fragment);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        if (articles != null) {
            holder.bindArticle(articles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public boolean swapData(ArrayList<News.Article> newDataList) {
        if (newDataList != null && newDataList.size() > 0) {
            this.articles.clear();
            this.articles.addAll(newDataList);
            this.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    public void addArticle(News.Article article){
        articles.add(article);
        notifyDataSetChanged();
    }

    public boolean addArticles(ArrayList<News.Article> newArticles) {
        if (newArticles.size() > 0) {
            articles.addAll(newArticles);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }


}
