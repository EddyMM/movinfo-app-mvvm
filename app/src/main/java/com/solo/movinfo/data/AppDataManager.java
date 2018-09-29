package com.solo.movinfo.data;


import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.responsemodels.VideosResponse;
import com.solo.movinfo.data.preferences.PreferencesHelper;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

public class AppDataManager implements DataManager {

    private PreferencesHelper mAppPreferencesHelper;

    @Inject
    AppDataManager(PreferencesHelper appPreferencesHelper) {
        mAppPreferencesHelper = appPreferencesHelper;
    }

    @Override
    public void setSplashScreenSeenByUser() {
        mAppPreferencesHelper.setSplashScreenSeen();
    }

    @Override
    public boolean wasSplashScreenSeen() {
        return mAppPreferencesHelper.wasSplashScreenSeen();
    }

    @Override
    public void setSortCriteria(String sortCriteria) {
        mAppPreferencesHelper.setSortCriteria(sortCriteria);
    }

    @Override
    public String getSortCriteria() {
        return mAppPreferencesHelper.getSortCriteria();
    }

    @Override
    public void loadVideos(int movieId,
            Callback<VideosResponse> videosCallback) {
        MovieDbService movieDbService = MovieDbApi.getInstance();

        Call<VideosResponse> videosResponseCall = movieDbService.getVideos(movieId);
        videosResponseCall.enqueue(videosCallback);
    }
}
