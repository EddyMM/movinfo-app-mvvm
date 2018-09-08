package com.solo.movinfo.data;


import android.arch.lifecycle.LiveData;

import com.solo.movinfo.data.network.models.Movie;

import java.util.List;

public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    LiveData<List<Movie>> getPopularMovies(int page);

    LiveData<List<Movie>> getTopRatedMovies(int page);

    String getSortCriteria();

}
