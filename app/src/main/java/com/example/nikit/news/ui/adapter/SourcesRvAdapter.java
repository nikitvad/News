package com.example.nikit.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.Source;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nikit on 15.03.2017.
 */

public class SourcesRvAdapter extends RecyclerView.Adapter<SourcesRvAdapter.SourceViewHolder> {
    private final ArrayList<Source> sources;

    public SourcesRvAdapter() {
        sources = new ArrayList<>();
    }

    public SourcesRvAdapter(ArrayList<Source> sources) {
        this.sources = sources;
    }

    public boolean swapData(ArrayList<Source> newData) {
        if (newData != null && newData.size() > 0) {
            sources.clear();
            sources.addAll(newData);
            this.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SourceViewHolder sourceViewHolder = new SourceViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.source_item, parent, false));

        return sourceViewHolder;
    }

    @Override
    public void onBindViewHolder(SourceViewHolder holder, int position) {
        holder.bindSource(sources.get(position));
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSourceName;
        private TextView tvSourceDesc;
        private TextView tvSourceCategory;
        private ImageView ivSourceLogo;


        public SourceViewHolder(View itemView) {
            super(itemView);
            tvSourceName = (TextView) itemView.findViewById(R.id.tv_source_name);
            tvSourceDesc = (TextView) itemView.findViewById(R.id.tv_source_desc);
            tvSourceCategory = (TextView) itemView.findViewById(R.id.tv_source_category);
            ivSourceLogo = (ImageView) itemView.findViewById(R.id.iv_source_image);
        }

        public void bindSource(Source source) {
            tvSourceName.setText(source.getName());
            tvSourceDesc.setText(source.getDescription());
            tvSourceCategory.setText(source.getCategory());
            Picasso.with(itemView.getContext()).load(source.getUrlsToLogos()
                    .getSmall()).into(ivSourceLogo);
        }
    }
}
