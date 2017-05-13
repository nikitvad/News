package com.example.nikit.news.ui.adapter.viewHolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nikit.news.Constants;
import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.ui.activity.WebViewActivity;
import com.example.nikit.news.util.Util;

/**
 * Created by nikit on 16.04.2017.
 */

public class SharedNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static int imageWidth = 0;
    public static int imageHeight = 0;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvComment;
    private TextView tvNewsTitle;
    private ImageView ivNewsImage;
    private TextView tvNewsDescription;

    private SharedNews sharedNews;

    public SharedNewsViewHolder(View itemView) {
        super(itemView);

        if (imageWidth == 0) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            imageWidth = Integer.parseInt(sharedPreferences.getString(Constants.PREF_KEY_IMAGE_SIZE, "1"));
            imageHeight = (int) (imageWidth / 1.77);
        }

        tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        ivUserAvatar = (ImageView) itemView.findViewById(R.id.iv_user_avatar);

        tvComment = (TextView) itemView.findViewById((R.id.tv_news_comment));

        tvNewsTitle = (TextView) itemView.findViewById(R.id.tv_shared_news_article_title);
        ivNewsImage = (ImageView) itemView.findViewById(R.id.iv_shared_news_image);
        tvNewsDescription = (TextView) itemView.findViewById(R.id.tv_shared_news_article_desc);

        tvNewsTitle.setOnClickListener(this);
    }

    public void bindSharedNews(SharedNews news) {
        this.sharedNews = news;
        tvUserName.setText(news.getFacebookUser().getName());
        Util.loadCircleImage(ivUserAvatar, news.getFacebookUser().getUrlToAvatar());
        tvComment.setText(news.getComment());

        tvNewsTitle.setText(news.getArticle().getTitle());
        tvNewsDescription.setText(news.getArticle().getDescription());
        Glide.with(itemView.getContext()).load(news.getArticle().getUrlToImage()).into(ivNewsImage);

        if (imageWidth > 1) {
            Glide.with(itemView.getContext()).load(news.getArticle().getUrlToImage()).dontAnimate()
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(imageWidth, imageHeight).centerCrop().into(ivNewsImage);
        } else {
            Glide.with(itemView.getContext()).load(news.getArticle().getUrlToImage()).dontAnimate()
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop().into(ivNewsImage);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shared_news_article_title:
                Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_NEWS_URL, sharedNews.getArticle().getUrl());
                itemView.getContext().startActivity(intent);
        }
    }
}
