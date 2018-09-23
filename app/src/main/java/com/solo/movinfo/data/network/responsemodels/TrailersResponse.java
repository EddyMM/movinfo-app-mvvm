package com.solo.movinfo.data.network.responsemodels;


import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.model.Video;

import java.util.List;

public class TrailersResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("results")
    private List<Video> mVideos;

    public String getId() {
        return id;
    }

    public List<Video> getVideos() {
        return mVideos;
    }
}
