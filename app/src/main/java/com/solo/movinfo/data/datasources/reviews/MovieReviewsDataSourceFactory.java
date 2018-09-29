package com.solo.movinfo.data.datasources.reviews;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.model.Review;

public class MovieReviewsDataSourceFactory extends DataSource.Factory<Integer, Review> {

    private int mMovieId;

    private MovieReviewsDataSource mMovieReviewsDataSource;

    public MovieReviewsDataSourceFactory(int id) {
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
