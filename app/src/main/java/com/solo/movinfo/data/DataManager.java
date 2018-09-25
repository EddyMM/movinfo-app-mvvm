package com.solo.movinfo.data;


import android.arch.lifecycle.LiveData;

import com.solo.movinfo.data.model.Video;
import com.solo.movinfo.data.network.responsemodels.VideosResponse;

import java.util.List;

import retrofit2.Callback;

public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    void setSortCriteria(String sortCriteria);

    String getSortCriteria();

    LiveData<List<Video>> getVideos(String movieId, Callback<VideosResponse> videosCallback);

}
