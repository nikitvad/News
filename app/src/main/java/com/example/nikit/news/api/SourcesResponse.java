package com.example.nikit.news.api;

import com.example.nikit.news.entities.Source;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nikit on 15.03.2017.
 */

public class SourcesResponse {

    private String status;
    private ArrayList<Source> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }


}
