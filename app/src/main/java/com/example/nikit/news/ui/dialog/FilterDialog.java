package com.example.nikit.news.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;


import com.example.nikit.news.R;
import com.example.nikit.news.database.DatabaseManager;
import com.example.nikit.news.database.SqLiteDbHelper;
import com.example.nikit.news.entities.Source;
import com.example.nikit.news.ui.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikit on 06.04.2017.
 */

public class FilterDialog extends DialogFragment {
    static List<String> listDataHeader;
    static HashMap<String, List<Source>> listDataChild;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private NoticeDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_list, null);

        expListView = (ExpandableListView) view.findViewById(R.id.elv_filter_list);

        prepareListData();

        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listAdapter.saveChanges();
                        mListener.onDialogPositiveClick(FilterDialog.this);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listAdapter.resetChanges();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataHeader.add("resources");

        listDataChild = new HashMap<String, List<Source>>();
        listDataChild.put(listDataHeader.get(0), new ArrayList<Source>());
        new LoadSourcesFromDbAsync().execute();

    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }


    class LoadSourcesFromDbAsync extends AsyncTask<Void, Void, Void> {
        SQLiteDatabase db;
        SqLiteDbHelper dbHelper;
        ArrayList<Source> sources;

        @Override
        protected void onPreExecute() {
            db = DatabaseManager.getInstance().openDatabase();
            dbHelper = new SqLiteDbHelper(getContext());
            sources = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DatabaseManager.getInstance().closeDatabase();
            listDataChild.put(listDataHeader.get(0), sources);
            Log.d("friend-list-idsss", sources.toString());

        }

        @Override
        protected Void doInBackground(Void... voids) {
             dbHelper.getAllSources(db, sources);
            return null;
        }
    }
}
