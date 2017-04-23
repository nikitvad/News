package com.example.nikit.news.ui.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.ui.GroupTitle;

/**
 * Created by nikit on 20.04.2017.
 */

public class TitleViewHolder extends RecyclerView.ViewHolder {
    private TextView tvTitle;

    public TitleViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView)itemView.findViewById(R.id.tv_group_title);
    }

    public void bindTitle(GroupTitle title){
        tvTitle.setText(title.getTitle());
    }

}
