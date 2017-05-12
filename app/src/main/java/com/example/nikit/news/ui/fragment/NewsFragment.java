package com.example.nikit.news.ui.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nikit.news.HidingScrollListener;
import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.ui.adapter.NewsRvAdapter;
import com.example.nikit.news.ui.dialog.FilterDialog;
import com.example.nikit.news.util.NetworkUtil;
import com.example.nikit.news.util.Prefs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private RecyclerView rvNews;
    private NewsRvAdapter newsRvAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View llInternetConnectionError;

    private LoadNewsAsyncTask newsAsyncTask;

    private SQLiteDatabase database;
    private SqLiteDbHelper sqLiteDbHelper;

    private Button btFilter;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sqLiteDbHelper = new SqLiteDbHelper(getContext());
        return inflater.inflate(R.layout.fragment_news_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newsRvAdapter.getItemCount() == 1) {
            updateContent();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llInternetConnectionError = view.findViewById(R.id.ll_no_newses_message);
        btFilter = (Button) view.findViewById(R.id.bt_filter);
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterDialog().show(getFragmentManager(), "tag");
            }
        });

        rvNews = (RecyclerView) view.findViewById(R.id.rv_articles);

        newsRvAdapter = new NewsRvAdapter(this);
        rvNews.setAdapter(newsRvAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        rvNews.addOnScrollListener(new HidingScrollListener(toolbar, tabLayout, btFilter));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setProgressViewOffset(false, toolbar.getHeight() * 2, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateContent();
            }
        });

    }

    public void updateContent() {
        if (isAdded()) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                if (newsAsyncTask != null && !newsAsyncTask.isCancelled()) {
                    newsAsyncTask.cancel(true);
                }
                newsAsyncTask = new LoadNewsAsyncTask();
                newsAsyncTask.execute();
            } else if (newsRvAdapter.getArticles().size() < 1) {
                new LoadArticlesFromDbAsync().execute();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoadNewsAsyncTask extends AsyncTask<Void, News, Void> {
        private Set<String> sourceIds;

        @Override
        protected void onPreExecute() {
            sourceIds = Prefs.getSourcesFilter();
            database = DatabaseManager.getInstance().openDatabase();
            llInternetConnectionError.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
            newsRvAdapter.clearData();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DatabaseManager.getInstance().closeDatabase();
            swipeRefreshLayout.setRefreshing(false);
            if (newsRvAdapter.getArticles().size() < 1) {
            }
            new SaveNewsesToDatabaseAsync().execute();

        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
            DatabaseManager.getInstance().closeDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity())) {

                Iterator<String> iterator = sourceIds.iterator();
                while (iterator.hasNext()) {
                    String sourceId = iterator.next();
                    News news = NetworkUtil.getNewsFromSource(sourceId, "top");

                    if (news != null && news.getArticles().size() > 0) {
                        for (News.Article item : news.getArticles()) {
                            if (sqLiteDbHelper.isLikedNewsContain(database, item.getArticleId())) {
                                item.setLiked(true);
                            } else {
                                item.setLiked(false);
                            }
                        }

                        publishProgress(news);
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
            } else if (newsRvAdapter.getArticles().size() < 1) {
                llInternetConnectionError.setVisibility(View.VISIBLE);
            }
            DatabaseManager.getInstance().closeDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            articles = sqLiteDbHelper.getAllArticles(database);
            return null;
        }
    }

    class SaveNewsesToDatabaseAsync extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase database;
        private SqLiteDbHelper dbHelper;

        @Override
        protected void onPreExecute() {
            dbHelper = new SqLiteDbHelper(getContext());
            database = DatabaseManager.getInstance().openDatabase();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dbHelper = null;
            DatabaseManager.getInstance().closeDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (newsRvAdapter.getArticles().size() > 0) {
                dbHelper.clearNewsTable(database);
                Log.d("saving data", "start");
                dbHelper.insertArticles(database, newsRvAdapter.getArticles());
            }

            return null;
        }
    }
}
