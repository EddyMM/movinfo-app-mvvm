package com.solo.movinfo.data.network.responsemodels;


import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.model.Video;

import java.util.List;

public class VideosResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("results")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }
}
