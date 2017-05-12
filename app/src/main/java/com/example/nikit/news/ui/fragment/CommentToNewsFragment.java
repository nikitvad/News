package com.example.nikit.news.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nikit.news.R;
import com.example.nikit.news.entities.facebook.FacebookUser;
import com.example.nikit.news.util.Util;
import com.example.nikit.news.util.facebook.LoadFacebookUserInfo;

public class CommentToNewsFragment extends Fragment {
    public static final String ARG_NEWS_ID = "news_id";
    public static final String ARG_NEWS_TITLE = "news_title";
    public static final String ARG_NEWS_IMAGE = "news_image";
    public static final String ARG_NEWS_DESC = "news_desc";


    private String newsId;
    private String newsTitle;
    private String newsDesc;
    private String newsImage;

    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvNewsTitle;
    private ImageView ivNewsImage;
    private TextView tvNewsDesc;
    private Button btSubmit;
    private EditText etComment;

    private OnCommentFragmentInteractionListener mListener;

    public CommentToNewsFragment() {
        // Required empty public constructor
    }

    public static CommentToNewsFragment newInstance(String newsId, String newsTitle, String newsDesc, String newsImage) {
        CommentToNewsFragment fragment = new CommentToNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_ID, newsId);
        args.putString(ARG_NEWS_TITLE, newsTitle);
        args.putString(ARG_NEWS_DESC, newsDesc);
        args.putString(ARG_NEWS_IMAGE, newsImage);
        fragment.setArguments(args);
        return fragment;
    }

    public static CommentToNewsFragment newInstance(Bundle args) {
        CommentToNewsFragment fragment = new CommentToNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsId = getArguments().getString(ARG_NEWS_ID);
            newsTitle = getArguments().getString(ARG_NEWS_TITLE);
            newsDesc = getArguments().getString(ARG_NEWS_DESC);
            newsImage = getArguments().getString(ARG_NEWS_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_to_news, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivUserAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);

        tvNewsTitle = (TextView) view.findViewById(R.id.tv_comment_fragment_article_title);
        ivNewsImage = (ImageView) view.findViewById(R.id.iv_comment_fragment_news_image);
        tvNewsDesc = (TextView) view.findViewById(R.id.tv_comment_fragment_article_desc);

        etComment = (EditText) view.findViewById(R.id.et_news_comment);

        btSubmit = (Button) view.findViewById(R.id.bt_comment_fragment_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onInteraction(etComment.getText().toString());
                }
            }
        });

        tvNewsTitle.setText(newsTitle);
        Glide.with(getContext()).load(newsImage).into(ivNewsImage);
        tvNewsDesc.setText(newsDesc);

        new LoadFacebookUserInfo(new LoadFacebookUserInfo.OnCompleteListener() {
            @Override
            public void onComplete(FacebookUser facebookUser) {
                Util.loadCircleImage(ivUserAvatar, facebookUser.getUrlToAvatar());
                tvUserName.setText(facebookUser.getName());
            }
        }).load();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommentFragmentInteractionListener) {
            mListener = (OnCommentFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCommentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCommentFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInteraction(String comment);
    }
}
