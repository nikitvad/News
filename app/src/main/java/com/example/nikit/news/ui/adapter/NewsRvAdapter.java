package com.example.nikit.news.ui.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.firebase.FirebaseDbHelper;
import com.example.nikit.news.ui.activity.WebViewActivity;
import com.example.nikit.news.ui.adapter.viewHolder.ArticleViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nikit on 14.03.2017.
 */

public class NewsRvAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private final ArrayList<News.Article> articles;

    private int imageHeight = 720;
    private int imageWidth = 480;

    public NewsRvAdapter() {
        articles = new ArrayList<>();
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
        return new ArticleViewHolder(inflater.inflate(R.layout.article_item, parent, false), this);
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

    public boolean addArticles(ArrayList<News.Article> newArticles) {
        if (newArticles.size() > 0) {
            articles.addAll(newArticles);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }


}
