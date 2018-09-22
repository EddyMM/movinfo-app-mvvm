package com.solo.movinfo.data.datasources;


import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.models.Movie;
import com.solo.movinfo.data.network.models.MoviesResponse;
import com.solo.movinfo.utils.Constants;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class MoviesListDataSource extends PageKeyedDataSource<Integer, Movie> {

    private MovieDbService mMovieDbService;
    private DataManager mDataManager;

    private LoadParams<Integer> mParams;
    private LoadCallback<Integer, Movie> mCallback;

    MoviesListDataSource(DataManager dataManager) {
        mMovieDbService = MovieDbApi.getInstance();
        mDataManager = dataManager;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
            @NonNull LoadInitialCallback<Integer, Movie> callback) {
        final int page = 1;

        Call<MoviesResponse> call;

        if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            call = mMovieDbService.getTopRatedMovies(page);
        } else {
            call = mMovieDbService.getPopularMovies(page);
        }

        try {
            Response<MoviesResponse> movieResponse = call.execute();

            MoviesResponse moviesResponse = movieResponse.body();
            if (moviesResponse == null) {
                Timber.e(new HttpException(movieResponse));
            } else {
                callback.onResult(moviesResponse.getResults(), 0,
                        Integer.parseInt(moviesResponse.getTotalPages()),
                        null, page + 1);
                Timber.d("Response: %s", moviesResponse.getResults());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {
        this.mParams = params;
        this.mCallback = callback;

        final int page = params.key;

        Call<MoviesResponse> call;

        if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            call = mMovieDbService.getTopRatedMovies(page);
        } else {
            call = mMovieDbService.getPopularMovies(page);
        }

        try {
            Response<MoviesResponse> movieResponse = call.execute();

            MoviesResponse moviesResponse = movieResponse.body();
            if (moviesResponse == null) {
                Timber.e(new HttpException(movieResponse));
            } else {
                Timber.d("Response: %s", moviesResponse.getResults());
                callback.onResult(moviesResponse.getResults(), page + 1);
            }
        } catch (IOException | IllegalStateException e) {
            Timber.e(e);
        }
    }


    public void retry() {
        Executor executor = Executors.newFixedThreadPool(3);
        executor.execute(()-> loadAfter(mParams, mCallback));
    }
}
