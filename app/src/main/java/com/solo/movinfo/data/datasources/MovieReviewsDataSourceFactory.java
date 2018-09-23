package com.solo.movinfo.data.datasources;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.model.Review;

public class MovieReviewsDataSourceFactory extends DataSource.Factory<Integer, Review> {

    private String mMovieId;

    private MovieReviewsDataSource mMovieReviewsDataSource;

    public MovieReviewsDataSourceFactory(String id) {
        mMovieId = id;
    }

    public MovieReviewsDataSource getMovieReviewsDataSource() {
        return mMovieReviewsDataSource;
    }

    @Override
    public DataSource<Integer, Review> create() {
        mMovieReviewsDataSource = new MovieReviewsDataSource(mMovieId);
        return mMovieReviewsDataSource;
    }

    public void retry() {
        mMovieReviewsDataSource.retry();
    }
}
