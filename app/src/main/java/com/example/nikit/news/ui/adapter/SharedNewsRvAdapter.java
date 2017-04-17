package com.example.nikit.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.ui.adapter.viewHolder.SharedNewsViewHolder;

import java.util.ArrayList;

/**
 * Created by nikit on 16.04.2017.
 */

public class SharedNewsRvAdapter extends RecyclerView.Adapter<SharedNewsViewHolder> {

    private ArrayList<SharedNews> newses;

    public SharedNewsRvAdapter() {
        newses = new ArrayList<>();
    }

    public SharedNewsRvAdapter(ArrayList<SharedNews> newses) {
        this.newses = newses;
    }

    @Override
    public SharedNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SharedNewsViewHolder viewHolder = new SharedNewsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shared_news_item, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SharedNewsViewHolder holder, int position) {
        holder.bindSharedNews(newses.get(position));
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    public void swapData(ArrayList<SharedNews> data) {
        if (data != null && data.size() > 0) {
            newses.clear();
            newses.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addShareNews(SharedNews news) {
        if (news != null) {
            newses.add(news);
            notifyDataSetChanged();
        }
    }
}
