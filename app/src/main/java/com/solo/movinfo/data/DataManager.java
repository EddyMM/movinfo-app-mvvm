package com.solo.movinfo.data;


import android.arch.lifecycle.LiveData;

import com.solo.movinfo.data.network.models.MoviesResponse;

import java.util.List;

import retrofit2.Response;

public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    LiveData<MoviesResponse> getPopularMovies(int page);

    LiveData<MoviesResponse> getTopRatedMovies(int page);

    String getSortCriteria();

}
