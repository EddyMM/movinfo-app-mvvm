package com.solo.movinfo.data.datasources;


import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.responsemodels.MoviesResponse;
import com.solo.movinfo.utils.Constants;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class MoviesListDataSource extends PageKeyedDataSource<Integer, Movie> {

    private DataManager mDataManager;

    private LoadParams<Integer> mParams;
    private LoadCallback<Integer, Movie> mCallback;

    MoviesListDataSource(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
            @NonNull LoadInitialCallback<Integer, Movie> callback) {
        final int page = Constants.MOVIES_LIST_INITIAL_PAGE_NUMBER;

        makeApiCall(callback, page);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {}

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {
        this.mParams = params;
        this.mCallback = callback;

        final int page = params.key;

        makeApiCall(callback, page);
    }

    /**
     * Fetches movies from the appropriate API endpoint
     * @param callback Callback to update observable with
     * @param page Page to load as is organized in the API
     */
    private void makeApiCall(Object callback, int page) {
        MovieDbService mMovieDbService = MovieDbApi.getInstance();

        Call<MoviesResponse> call;

        // Decide which endpoint to call based on user preferences
        if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            call = mMovieDbService.getTopRatedMovies(page);
        } else {
            call = mMovieDbService.getPopularMovies(page);
        }

        try {
            Response<MoviesResponse> response = call.execute();

            MoviesResponse moviesResponse = response.body();
            if (moviesResponse == null) {
                Timber.e(new HttpException(response));
            } else {
                Timber.d("Response: %s", moviesResponse.getResults());

                if (callback instanceof LoadInitialCallback) {
                    //noinspection unchecked
                    ((LoadInitialCallback) callback).onResult(moviesResponse.getResults(), 0,
                            Integer.parseInt(moviesResponse.getTotalResults()),
                            null, page + 1);
                } else if (callback instanceof LoadCallback) {
                    //noinspection unchecked
                    ((LoadCallback) callback).onResult(moviesResponse.getResults(), page + 1);
                } else {
                    Timber.e(new Exception("Unknown callback supplied for Movies List API call"));
                }
            }
        } catch (IOException | IllegalStateException e) {
            Timber.e(e);
        }
    }


    /**
     * Continues loading movies from the last page stored in state
     */
    public void retry() {
        Executor executor = Executors.newFixedThreadPool(3);
        executor.execute(() -> loadAfter(mParams, mCallback));
    }
}
