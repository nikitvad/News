package com.example.nikit.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikit.news.R;
import com.example.nikit.news.entities.facebook.User;

import java.util.ArrayList;

/**
 * Created by nikit on 15.04.2017.
 */

public class FriendsRvAdapter extends RecyclerView.Adapter<FriendsRvAdapter.FriendViewHolder> {
    private ArrayList<User> users;
    private OnClickListener mListener;

    public FriendsRvAdapter() {
        users = new ArrayList<>();
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
        holder.bindFriend(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void swapData(ArrayList<User> newUserList) {
        if (newUserList != null) {
            users.clear();
            users.addAll(newUserList);
            notifyDataSetChanged();
        }
    }

    public void addFriend(User user) {
        if (user != null) {
            users.add(user);
            notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onClick(View v, User user);
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

        public void bindFriend(User user) {
            Glide.with(itemView.getContext()).load(user.getUrlToAvatar()).into(ivAvatar);
            tvName.setText(user.getName());

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                if(!cbSelected.isChecked()) {
                    cbSelected.setChecked(true);
                    mListener.onClick(view, users.get(getAdapterPosition()));
                    Toast.makeText(view.getContext(), "Offf", Toast.LENGTH_SHORT).show();
                }else{
                    cbSelected.setChecked(false);
                    mListener.onClick(view, users.get(getAdapterPosition()));
                    Toast.makeText(view.getContext(), "Ogg", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
