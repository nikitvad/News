package com.example.nikit.news.ui.fragment;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nikit.news.Constants;
import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.NewsRvAdapter;
import com.example.nikit.news.ui.dialog.FilterDialog;
import com.example.nikit.news.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class RetrofitFragment extends Fragment {
    private RecyclerView rvNews;
    private NewsRvAdapter newsRvAdapter;
    private SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LoadNewsAsyncTask newsAsyncTask;

    private SQLiteDatabase database;
    private SqLiteDbHelper sqLiteDbHelper;

    private Button btFilter;

    public RetrofitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sqLiteDbHelper = new SqLiteDbHelper(getContext());
        preferences = container.getContext().getSharedPreferences(
                container.getContext().getPackageName(), container.getContext().MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_retrofit, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(newsRvAdapter.getItemCount()==0){
            updateContent();
        }
        if(newsRvAdapter.getItemCount()<1) {
            new LoadNewsAsyncTask().execute();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNews = (RecyclerView) view.findViewById(R.id.rv_articles);
        rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                ActionBar ab = getActivity().getActionBar();
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

        newsRvAdapter = new NewsRvAdapter();
        rvNews.setAdapter(newsRvAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(view.getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateContent();
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        btFilter = (Button) view.findViewById(R.id.bt_filter);
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterDialog().show(getFragmentManager(), "tag");
            }
        });

    }

    public void updateContent() {
        if (isAdded()) {
            if(newsAsyncTask!=null && !newsAsyncTask.isCancelled()){
                newsAsyncTask.cancel(true);
            }
            newsAsyncTask = new LoadNewsAsyncTask();
            newsAsyncTask.execute();
        }
    }


    class LoadNewsAsyncTask extends AsyncTask<Void, News, Void> {
        private Set<String> sourceIds;

        @Override
        protected void onPreExecute() {
            sourceIds = preferences.getStringSet(Constants.FILTER_SOURCES_TAG, new HashSet<String>());
            database = DatabaseManager.getInstance().openDatabase();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DatabaseManager.getInstance().closeDatabase();
            new LoadArticlesFromDbAsync().execute();
        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
            DatabaseManager.getInstance().closeDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (NetworkUtil.isNetworkAvailable(getActivity())) {

                if (sourceIds.size() > 0) {
                    sqLiteDbHelper.clearNewsTable(database);
                } else {
                    return null;
                }
                Iterator<String> iterator = sourceIds.iterator();
                while (iterator.hasNext()) {
                    String sourceId = iterator.next();
                    News news = NetworkUtil.getNewsFromSource(sourceId, "top");

                    if (news != null && news.getArticlesCount() > 0) {
                        sqLiteDbHelper.insertArticles(database, news.getArticles());
                    }
                }

            } else {
                return null;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(News... values) {
            newsRvAdapter.addArticles(values[0].getArticles());
        }
    }

    class LoadArticlesFromDbAsync extends AsyncTask<Void, Void, Void> {
        private ArrayList<News.Article> articles;

        @Override
        protected void onPreExecute() {
            database = DatabaseManager.getInstance().openDatabase();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (articles.size() > 0) {
                newsRvAdapter.swapData(articles);
            }
            DatabaseManager.getInstance().closeDatabase();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            articles = sqLiteDbHelper.getAllArticles(database);
            return null;
        }
    }
}
