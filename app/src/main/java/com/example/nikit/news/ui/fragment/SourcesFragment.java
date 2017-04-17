package com.example.nikit.news.ui.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikit.news.R;
import com.example.nikit.news.ui.adapter.SourcesRvAdapter;
import com.example.nikit.news.util.JsonUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourcesFragment extends Fragment {
    private RecyclerView rvSources;
    private final SourcesRvAdapter sourcesAdapter;

    public SourcesFragment() {
        sourcesAdapter = new SourcesRvAdapter();
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sources, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSources = (RecyclerView) view.findViewById(R.id.rv_sources);
        rvSources.setAdapter(sourcesAdapter);
        rvSources.setLayoutManager(new LinearLayoutManager(view.getContext()));

        new LoadNewsAsyncTask().execute();

    }

    // Loading news from network
    class LoadNewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private String jsonString;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (jsonString != null && jsonString.length() > 0) {
                sourcesAdapter.swapData(JsonUtil.getSourcesFromJson(jsonString));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = JsonUtil.loadJsonString("https://newsapi.org/v1/sources");
            return null;
        }
    }


}
