package com.solo.movinfo.data.datasources.base;


import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.data.network.responsemodels.MoviesResponse;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public abstract class PagedMoviesDataSource extends PageKeyedDataSource<Integer, Movie> {

    private LoadParams<Integer> mParams;
    private LoadCallback<Integer, Movie> mCallback;

    /**
     * Fetches movies from the appropriate API endpoint
     *
     * @param callback Callback to update observable with
     * @param page     Page to load as is organized in the API
     */
    protected void makeApiCall(Call<MoviesResponse> call, Object callback, int page) {

        try {
            Response<MoviesResponse> response = call.execute();

            MoviesResponse moviesResponse = response.body();
            if (moviesResponse == null) {
                Timber.e(new HttpException(response));
            } else {
                Timber.d("Response: %s", moviesResponse.getResults());

                if (callback instanceof PageKeyedDataSource.LoadInitialCallback) {
                    //noinspection unchecked
                    ((PageKeyedDataSource.LoadInitialCallback) callback).onResult(
                            moviesResponse.getResults(), 0,
                            Integer.parseInt(moviesResponse.getTotalResults()),
                            null, page + 1);
                } else if (callback instanceof PageKeyedDataSource.LoadCallback) {
                    //noinspection unchecked
                    ((PageKeyedDataSource.LoadCallback) callback).onResult(
                            moviesResponse.getResults(), page + 1);
                } else {
                    Timber.e(new Exception("Unknown callback supplied for Movies List API call"));
                }
            }
        } catch (IOException | IllegalStateException e) {
            Timber.e(e);
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {
        this.mParams = params;
        this.mCallback = callback;
    }

    /**
     * Continues loading movies from the last page stored in state
     */
    public void retry() {
        Executor executor = Executors.newFixedThreadPool(3);
        if (mParams != null && mCallback != null) {
            executor.execute(() -> loadAfter(mParams, mCallback));
        }
    }
}
