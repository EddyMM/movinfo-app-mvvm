package com.solo.movinfo.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.models.MoviesResponse;
import com.solo.movinfo.data.preferences.PreferencesHelper;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
    public LiveData<MoviesResponse> getPopularMovies(int page) {
        Timber.d("Making an API call for popular movies");

        final MutableLiveData<MoviesResponse> moviesResponseMutableLiveData =
                new MutableLiveData<>();

        MovieDbService movieDbService = MovieDbApi.getInstance(page);
        Call<MoviesResponse> popularMoviesCall = movieDbService.getPopularMovies();

        popularMoviesCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call,
                    @NonNull Response<MoviesResponse> response) {
                Timber.d("Received movies: %s", response.body());
                moviesResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Timber.e(t);
                moviesResponseMutableLiveData.setValue(null);
            }
        });

        return moviesResponseMutableLiveData;
    }

    @Override
    public Response<MoviesResponse> getTopRatedMovies(int page) {
        MovieDbService movieDbService = MovieDbApi.getInstance(page);

        Call<MoviesResponse> topRatedMoviesCall = movieDbService.getTopRatedMovies();
        try {
            return topRatedMoviesCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSortCriteria() {
        return mAppPreferencesHelper.getSortCriteria();
    }
}
