package com.solo.movinfo.data;


import com.solo.movinfo.data.network.responsemodels.VideosResponse;

import retrofit2.Callback;

public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    void setSortCriteria(String sortCriteria);

    String getSortCriteria();

    void loadVideos(int movieId, Callback<VideosResponse> videosCallback);

}
