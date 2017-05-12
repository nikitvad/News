package com.example.nikit.news.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.firebase.SharedNews;
import com.example.nikit.news.ui.fragment.AvailableFriendsFragment;
import com.example.nikit.news.ui.fragment.CommentToNewsFragment;
import com.example.nikit.news.util.firebase.FirebaseNewsManager;
import com.facebook.AccessToken;

import java.util.HashSet;

public class ShareByAppActivity extends AppCompatActivity
        implements AvailableFriendsFragment.OnFragmentInteractionListener,
        CommentToNewsFragment.OnCommentFragmentInteractionListener {

    private String newsId;
    private Toolbar toolbar;

    private HashSet<String> selectedFriends;
    private CommentToNewsFragment commentToNews;
    private AvailableFriendsFragment availableFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_by_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("ARGS");
        if (bundle != null) {
            newsId = bundle.getString(CommentToNewsFragment.ARG_NEWS_ID, "");

            commentToNews = CommentToNewsFragment.newInstance(getIntent().getBundleExtra("ARGS"));
            availableFriends = AvailableFriendsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.share_container, availableFriends).commit();

        } else {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(HashSet<String> uid) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.share_container, commentToNews).commit();
        selectedFriends = uid;
    }

    @Override
    public void onInteraction(String comment) {
        if (!newsId.isEmpty() && newsId.length() > 0) {
            SharedNews sharedNews = new SharedNews();
            sharedNews.setComment(comment);
            sharedNews.setNewsId(newsId);

            for (String item : selectedFriends) {
                FirebaseNewsManager.shareNewsWithFriend(AccessToken.getCurrentAccessToken().getUserId(), item, sharedNews);
            }
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
