package com.example.nikit.news.ui.adapter.viewHolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nikit.news.Constants;
import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.activity.ShareByAppActivity;
import com.example.nikit.news.ui.activity.WebViewActivity;
import com.example.nikit.news.ui.fragment.CommentToNewsFragment;
import com.example.nikit.news.util.Prefs;
import com.example.nikit.news.util.firebase.FirebaseNewsManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.auth.FirebaseAuth;
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
    private TextView tvLikesCount;

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabShareByApp;
    private FloatingActionButton fabShareByFacebook;
    private FloatingActionButton fabShareByGPlus;

    private SQLiteDatabase sqLiteDatabase;
    private SqLiteDbHelper sqLiteDbHelper;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView.Adapter adapter;
    private Fragment fragment;

    public static void setImageWidth(int imageWidth) {
        ArticleViewHolder.imageWidth = imageWidth;
    }

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
        tvLikesCount = (TextView) itemView.findViewById(R.id.tv_likes_count);

        if (imageWidth == 0) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            imageWidth = Integer.parseInt(sharedPreferences.getString(Constants.PREF_KEY_IMAGE_SIZE, "100"));
            imageHeight = (int) (imageWidth / 1.77);
        }

    }

    public void bindArticle(final News.Article article) {
        this.article = article;

        tvArticleTitle.setText(article.getTitle());
        tvArticleDesc.setText(article.getDescription());

        FirebaseNewsManager.getLikesCount(article.getArticleId(), new FirebaseNewsManager.OnResultListener() {
            @Override
            public void onResult(long count) {
                tvLikesCount.setText(count + "");
            }
        });

        if (imageWidth > 1) {
            Glide.with(itemView.getContext()).load(article.getUrlToImage()).dontAnimate()
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(imageWidth, imageHeight).centerCrop().into(ivArticleImage);
        } else {
            Glide.with(itemView.getContext()).load(article.getUrlToImage()).dontAnimate()
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop().into(ivArticleImage);
        }

        if (!article.isLiked()) {
            ivLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            ivLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        }


        ivLike.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_share_by_app:
                if (Prefs.getLoggedType() == Prefs.FACEBOOK_LOGIN) {
                    FirebaseNewsManager.pushNews(article);
                    Bundle args = new Bundle();
                    args.putString(CommentToNewsFragment.ARG_NEWS_ID, article.getArticleId());
                    args.putString(CommentToNewsFragment.ARG_NEWS_TITLE, article.getTitle());
                    args.putString(CommentToNewsFragment.ARG_NEWS_IMAGE, article.getUrlToImage());
                    args.putString(CommentToNewsFragment.ARG_NEWS_DESC, article.getDescription());
                    Intent intent = new Intent(itemView.getContext(), ShareByAppActivity.class);
                    intent.putExtra("ARGS", args);
                    itemView.getContext().startActivity(intent);
                    floatingActionMenu.close(false);
                    return;
                } else {
                    Toast.makeText(view.getContext(), R.string.share_available_for_facebook_users, Toast.LENGTH_SHORT).show();
                    return;
                }

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

            case R.id.iv_like:
                likeNews(article);
                return;

            default:
                floatingActionMenu.close(true);
                return;
        }
    }

    private void likeNews(final News.Article article) {
        if (firebaseAuth.getCurrentUser() != null) {
            if (!article.isLiked()) {

                FirebaseNewsManager.likeNews(article, new FirebaseNewsManager.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        article.setLiked(true);
                        sqLiteDbHelper.addLikedNews(sqLiteDatabase, article.getArticleId());
                        adapter.notifyItemChanged(getAdapterPosition());
                    }
                });

            } else {

                FirebaseNewsManager.unlikeNews(article, new FirebaseNewsManager.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        article.setLiked(false);
                        sqLiteDbHelper.removeLikedNews(sqLiteDatabase, article.getArticleId());
                        adapter.notifyItemChanged(getAdapterPosition());

                    }
                });


            }
        }
    }
}
