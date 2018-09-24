package com.solo.movinfo.data.datasources;


import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.solo.movinfo.data.model.Review;
import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.responsemodels.ReviewsResponse;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class MovieReviewsDataSource extends PageKeyedDataSource<Integer, Review> {

    private String mMovieId;
    private LoadParams<Integer> mParams;
    private LoadCallback<Integer, Review> mCallback;

    MovieReviewsDataSource(String id) {
        mMovieId = id;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
            @NonNull LoadInitialCallback<Integer, Review> callback) {

        final int page = 1;
        MovieDbService movieDbService = MovieDbApi.getInstance();

        Call<ReviewsResponse> reviewsResponseCall = movieDbService.getReviews(mMovieId, page);

        try {
            Response<ReviewsResponse> response = reviewsResponseCall.execute();

            ReviewsResponse reviewsResponse = response.body();
            if (reviewsResponse == null) {
                Timber.e(new HttpException(response));
            } else {
                Timber.d("Response(%s): %s", reviewsResponse.getReviews().size(),
                        reviewsResponse.getReviews());
                callback.onResult(reviewsResponse.getReviews(), 0,
                        reviewsResponse.getTotalResults(),
                        null, page + 1);
            }
        } catch (IllegalStateException | IOException e) {
            Timber.e(e);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Review> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Review> callback) {
        this.mParams = params;
        this.mCallback = callback;

        final int page = params.key;

        MovieDbService movieDbService = MovieDbApi.getInstance();
        Call<ReviewsResponse> reviewsResponseCall = movieDbService.getReviews(mMovieId, page);

        try {
            Response<ReviewsResponse> response = reviewsResponseCall.execute();

            ReviewsResponse reviewsResponse = response.body();
            if (reviewsResponse == null) {
                Timber.e(new HttpException(response));
            } else {
                Timber.d("Response: %s", reviewsResponse.getReviews());
                callback.onResult(reviewsResponse.getReviews(), page + 1);
            }
        } catch (IllegalStateException | IOException e) {
            Timber.e(e);
        }
    }

    void retry() {
        Executor executor = Executors.newFixedThreadPool(3);
        executor.execute(() -> loadAfter(mParams, mCallback));
    }
}
