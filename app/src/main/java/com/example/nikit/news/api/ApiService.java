package com.example.nikit.news.api;

import android.support.annotation.Nullable;

//import com.example.nikit.news.entities.Article;
import com.example.nikit.news.entities.News;
import com.example.nikit.news.entities.Source;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nikit on 18.03.2017.
 */

public interface ApiService {
    @GET("v1/articles")
    Call<News> getNewsEntity(@Query("source") String sourceId,
                             @Nullable @Query("sortBy") String sortBy,
                             @Query("apiKey") String apiKey);

    @GET("v1/sources")
    Call<SourcesResponse> getSources(@Nullable @Query("category") String category,
                                     @Nullable @Query("language") String language,
                                     @Nullable @Query("country") String country);

}