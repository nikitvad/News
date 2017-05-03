package com.example.nikit.news.ui.adapter;

import android.content.res.Resources;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.viewHolder.ArticleViewHolder;

import java.util.ArrayList;

/**
 * Created by nikit on 14.03.2017.
 */

public class NewsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
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
        imageHeight = (int) (imageWidth / 1.77);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            return new ArticleViewHolder(inflater.inflate(R.layout.article_item, parent, false), this, fragment);
        } else if (viewType == TYPE_HEADER) {
            final View view = inflater.inflate(R.layout.recycler_view_header, parent, false);
            return new RecyclerHeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((ArticleViewHolder) holder).bindArticle(articles.get(position - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return articles.size() + 1;
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

    public ArrayList<News.Article> getArticles() {
        return articles;
    }

    public void clearData() {
        articles.clear();
        notifyDataSetChanged();
    }

    public void addArticle(News.Article article) {
        articles.add(article);
        notifyItemInserted(getItemCount());
        //notifyDataSetChanged();
    }

    public boolean addArticles(ArrayList<News.Article> newArticles) {
        if (newArticles.size() > 0) {
            int startPosition = getItemCount();
            articles.addAll(newArticles);
            //notifyDataSetChanged();
            notifyItemRangeInserted(startPosition, newArticles.size());
            return true;
        }
        return false;
    }

    class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {
        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}
