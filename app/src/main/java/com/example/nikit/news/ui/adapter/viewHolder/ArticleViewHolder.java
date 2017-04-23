package com.example.nikit.news.ui.adapter.viewHolder;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nikit.news.R;
import com.example.nikit.news.application.NewsApplication;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.activity.ShareByApp;
import com.example.nikit.news.ui.activity.WebViewActivity;
import com.example.nikit.news.ui.fragment.CommentToNews;
import com.example.nikit.news.util.firebase.FirebaseNewsManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nikit on 11.04.2017.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    private static int imageHeight = 0;
    private static int imageWidth = 0;
    private News.Article article;
    private TextView tvArticleTitle;
    private ImageView ivArticleImage;
    private TextView tvArticleDesc;
    private ImageView ivLike;

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabShareByApp;
    private FloatingActionButton fabShareByFacebook;
    private FloatingActionButton fabShareByGPlus;


    private SQLiteDatabase sqLiteDatabase;
    private SqLiteDbHelper sqLiteDbHelper;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView.Adapter adapter;
    private Fragment fragment;

    public ArticleViewHolder(View itemView, RecyclerView.Adapter adapter, Fragment fragment) {
        super(itemView);

        this.adapter = adapter;
        this.fragment = fragment;

        itemView.setOnClickListener(this);

        sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDbHelper = new SqLiteDbHelper(itemView.getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        floatingActionMenu = (FloatingActionMenu) itemView.findViewById(R.id.fam_share_menu);
        fabShareByApp = (FloatingActionButton) itemView.findViewById(R.id.menu_share_by_app);
        fabShareByFacebook = (FloatingActionButton) itemView.findViewById(R.id.menu_share_by_facebook);
        fabShareByGPlus = (FloatingActionButton) itemView.findViewById(R.id.share_by_google_plus);

        tvArticleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
        tvArticleDesc = (TextView) itemView.findViewById(R.id.tv_article_desc);
        ivArticleImage = (ImageView) itemView.findViewById(R.id.iv_article_image);
        ivLike = (ImageView) itemView.findViewById(R.id.iv_like);

        if (imageWidth == 0) {
            SharedPreferences preferences = itemView.getContext().getSharedPreferences(
                    itemView.getContext().getPackageName(), Context.MODE_PRIVATE);
            imageWidth = preferences.getInt("display_width", 1080);
            imageHeight = (int) (imageWidth / 1.77);
        }

    }

    public void bindArticle(final News.Article article) {
        this.article = article;

        tvArticleTitle.setText(article.getTitle());
        tvArticleDesc.setText(article.getDescription());

        Glide.with(itemView.getContext()).load(article.getUrlToImage()).dontAnimate()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(imageWidth, imageHeight).centerCrop().into(ivArticleImage);
        if (!article.isLiked()) {
            ivLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            ivLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (!article.isLiked()) {
                        databaseReference = firebaseDatabase.getReference("news");
                        databaseReference.child(article.getArticleId()).setValue(article.toMap());

                        databaseReference = firebaseDatabase.getReference("users");
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("liked-news")
                                .child(article.getArticleId()).setValue("true");

                        article.setLiked(true);
                        sqLiteDbHelper.addLikedNews(sqLiteDatabase, article.getArticleId());
                        adapter.notifyItemChanged(getAdapterPosition());

                    } else {

                        article.setLiked(false);
                        databaseReference = FirebaseDatabase.getInstance()
                                .getReference("users/" + firebaseAuth.getCurrentUser().getUid() + "/liked-news");
                        databaseReference.child(article.getArticleId()).removeValue();
                        sqLiteDbHelper.removeLikedNews(sqLiteDatabase, article.getArticleId());
                        adapter.notifyItemChanged(getAdapterPosition());

                    }
                }
            }
        });

        fabShareByFacebook.setOnClickListener(this);
        fabShareByApp.setOnClickListener(this);
        fabShareByGPlus.setOnClickListener(this);
        floatingActionMenu.close(false);


        tvArticleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_NEWS_URL, article.getUrl());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private void showShareMenu(final View v, final News.Article article) {
        PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
        popupMenu.inflate(R.menu.share_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_menu_by_app:
                        Bundle args = new Bundle();
                        args.putString(CommentToNews.ARG_NEWS_ID, article.getArticleId());
                        args.putString(CommentToNews.ARG_NEWS_TITLE, article.getTitle());
                        args.putString(CommentToNews.ARG_NEWS_IMAGE, article.getUrlToImage());
                        args.putString(CommentToNews.ARG_NEWS_DESC, article.getDescription());
                        Intent intent = new Intent(v.getContext(), ShareByApp.class);
                        intent.putExtra("ARGS", args);
                        v.getContext().startActivity(intent);
                        return true;

                    case R.id.share_menu_by_facebook:
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(article.getUrl())).build();
                        ShareDialog.show(fragment, content);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_share_by_app:
                Bundle args = new Bundle();
                args.putString(CommentToNews.ARG_NEWS_ID, article.getArticleId());
                args.putString(CommentToNews.ARG_NEWS_TITLE, article.getTitle());
                args.putString(CommentToNews.ARG_NEWS_IMAGE, article.getUrlToImage());
                args.putString(CommentToNews.ARG_NEWS_DESC, article.getDescription());
                Intent intent = new Intent(itemView.getContext(), ShareByApp.class);
                intent.putExtra("ARGS", args);
                itemView.getContext().startActivity(intent);
                floatingActionMenu.close(false);

                return;

            case R.id.menu_share_by_facebook:
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(article.getUrl())).build();
                ShareDialog.show(fragment, content);
                floatingActionMenu.close(false);

                return;

            case R.id.fam_share_menu:
                floatingActionMenu.toggle(true);
                return;
            case R.id.share_by_google_plus:
                Intent shareIntent = new PlusShare.Builder(itemView.getContext())
                        .setType("text/plain")
                        .setContentUrl(Uri.parse(article.getUrl()))
                        .getIntent();
                itemView.getContext().startActivity(shareIntent);
                floatingActionMenu.close(false);
                return;


            default:
                floatingActionMenu.close(true);
                return;
        }
    }
}
