package com.example.nikit.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.entities.ui.GroupTitle;
import com.example.nikit.news.entities.ui.ListItem;
import com.example.nikit.news.ui.adapter.viewHolder.SharedNewsViewHolder;
import com.example.nikit.news.ui.adapter.viewHolder.TitleViewHolder;

import java.util.ArrayList;

/**
 * Created by nikit on 16.04.2017.
 */

public class NewsesFromFriendsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int newNewsesCount = -1;
    private int oldNewsesCount = -1;

    private ArrayList<ListItem> items;

    public NewsesFromFriendsRvAdapter() {

        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == ListItem.DEF_ITEM) {
            viewHolder = new SharedNewsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shared_news_item, parent, false));

        } else if (viewType == ListItem.TITLE_ITEM) {
            viewHolder = new TitleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_title_item, parent, false));

        } else viewHolder = null;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SharedNewsViewHolder) {
            ((SharedNewsViewHolder) holder).bindSharedNews((SharedNews) items.get(position));
        } else if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).bindTitle((GroupTitle) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addShareNews(SharedNews news) {
        if (news.getNewsType().equals(SharedNews.NEWS_TYPE_DEF)) {
            if (oldNewsesCount > -1) {
                items.add(newNewsesCount + 2, news);
                notifyItemInserted(newNewsesCount + 2);
                oldNewsesCount++;
            } else {
                items.add(newNewsesCount + 1, new GroupTitle("old"));
                notifyItemInserted(newNewsesCount + 1);
                oldNewsesCount = 0;
                items.add(newNewsesCount + 2, news);
                notifyItemInserted(newNewsesCount + 2);
            }
        } else {
            if (newNewsesCount > -1) {
                items.add(1, news);
                notifyItemInserted(1);
                newNewsesCount++;
            } else {
                items.add(0, new GroupTitle("new"));
                notifyItemInserted(0);
                newNewsesCount=0;
                items.add(1, news);
                notifyItemInserted(1);
                newNewsesCount++;
            }
        }
    }

    public void clearData(){
        newNewsesCount=-1;
        oldNewsesCount=-1;
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}
