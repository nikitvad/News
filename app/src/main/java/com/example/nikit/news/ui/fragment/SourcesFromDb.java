package com.example.nikit.news.ui.fragment;


import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.nikit.news.R;
import com.example.nikit.news.api.ApiClient;
import com.example.nikit.news.api.SourcesResponse;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.Source;
import com.example.nikit.news.ui.adapter.SourcesRvAdapter;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SourcesFromDb extends Fragment {
    private Button btUpdateDb;
    private Button btShowData;
    private RecyclerView rvSourcesList;
    private final SourcesRvAdapter adapter;
    private SQLiteDatabase db;


    private SqLiteDbHelper sqLiteDbHelper;

    public SourcesFromDb() {
        adapter = new SourcesRvAdapter();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sources_from_db, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btUpdateDb = (Button) view.findViewById(R.id.update_db);
        btShowData = (Button) view.findViewById(R.id.show_sources);
        rvSourcesList = (RecyclerView) view.findViewById(R.id.rv_sources_from_dp);
        rvSourcesList.setAdapter(adapter);
        rvSourcesList.setLayoutManager(new LinearLayoutManager(getContext()));

        sqLiteDbHelper = new SqLiteDbHelper(getContext());

        db = DatabaseManager.getInstance().openDatabase();

        btUpdateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                new UpdateDbAsync().execute();

            }
        });

        btShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Source> sources = new ArrayList<Source>();
                sqLiteDbHelper.getAllSources(db, sources);
                adapter.swapData(sources);
            }
        });

    }


    class UpdateDbAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Response<SourcesResponse> response = ApiClient.getInstance().getSources(null, null, null).execute();
                if (response.isSuccessful() && response.body() != null) {

                    sqLiteDbHelper.insertSource(db, response.body().getSources());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
