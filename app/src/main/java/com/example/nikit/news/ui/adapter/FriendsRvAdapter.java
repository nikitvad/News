package com.example.nikit.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikit.news.R;
import com.example.nikit.news.entities.facebook.FacebookUser;
import com.example.nikit.news.util.Util;

import java.util.ArrayList;

/**
 * Created by nikit on 15.04.2017.
 */

public class FriendsRvAdapter extends RecyclerView.Adapter<FriendsRvAdapter.FriendViewHolder> {
    private ArrayList<FacebookUser> facebookUsers;
    private OnClickListener mListener;

    public FriendsRvAdapter() {
        facebookUsers = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendViewHolder viewHolder = new FriendViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bindFriend(facebookUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return facebookUsers.size();
    }

    public void swapData(ArrayList<FacebookUser> newFacebookUserList) {
        if (newFacebookUserList != null) {
            facebookUsers.clear();
            facebookUsers.addAll(newFacebookUserList);
            notifyDataSetChanged();
        }
    }

    public void addFriend(FacebookUser facebookUser) {
        if (facebookUser != null) {
            facebookUsers.add(facebookUser);
            notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onClick(View v, FacebookUser facebookUser);
    }


    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivAvatar;
        private TextView tvName;
        private CheckBox cbSelected;

        public FriendViewHolder(View itemView) {
            super(itemView);

            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_friend_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cb_friend_selected);

            itemView.setOnClickListener(this);
        }

        public void bindFriend(FacebookUser facebookUser) {
            Util.loadCircleImage(ivAvatar, facebookUser.getUrlToAvatar());
            tvName.setText(facebookUser.getName());

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                if (!cbSelected.isChecked()) {
                    cbSelected.setChecked(true);
                    mListener.onClick(view, facebookUsers.get(getAdapterPosition()));
                } else {
                    cbSelected.setChecked(false);
                    mListener.onClick(view, facebookUsers.get(getAdapterPosition()));

                }
            }
        }
    }
}
