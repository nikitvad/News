package com.example.nikit.news.ui.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;

/**
 * Created by nikit on 16.04.2017.
 */

public class SharedNewsViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivUserAvatar;
    private TextView tvUserName;

    private TextView tvNewsTitle;
    private ImageView ivNewsImage;
    private TextView tvNewsDescription;

    public SharedNewsViewHolder(View itemView) {
        super(itemView);

        tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        ivUserAvatar = (ImageView) itemView.findViewById(R.id.iv_user_avatar);

        tvNewsTitle = (TextView) itemView.findViewById(R.id.tv_shared_news_article_title);
        ivNewsImage = (ImageView) itemView.findViewById(R.id.iv_shared_news_image);
        tvNewsDescription = (TextView) itemView.findViewById(R.id.tv_shared_news_article_desc);
    }

    public void bindSharedNews(SharedNews news) {
        tvUserName.setText(news.getUser().getName());
        Glide.with(itemView.getContext()).load(news.getUser().getUrlToAvatar()).into(ivUserAvatar);

        tvNewsTitle.setText(news.getArticle().getTitle());
        tvNewsDescription.setText(news.getArticle().getDescription());
        Glide.with(itemView.getContext()).load(news.getArticle().getUrlToImage()).into(ivNewsImage);
    }
}
