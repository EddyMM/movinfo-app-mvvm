package com.solo.movinfo.ui.movies.detail;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.solo.movinfo.data.datasources.MovieReviewsDataSourceFactory;
import com.solo.movinfo.data.model.Review;
import com.solo.movinfo.data.model.Video;

public class MoviesDetailViewModel extends ViewModel {

    private String mMovieId;

    private LiveData<PagedList<Video>> videosLiveData;
    private LiveData<PagedList<Review>> reviewsLiveData;

    private MovieReviewsDataSourceFactory mMovieReviewsDataSourceFactory;

    MoviesDetailViewModel(String id) {
        mMovieId = id;

        fetchReviews();

        //fetchVideos();
    }

    private void fetchReviews() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        mMovieReviewsDataSourceFactory =
                new MovieReviewsDataSourceFactory(mMovieId);

        reviewsLiveData = new LivePagedListBuilder<>(mMovieReviewsDataSourceFactory, config)
                .build();
    }


    public LiveData<PagedList<Video>> getVideosLiveData() {
        return videosLiveData;
    }

    LiveData<PagedList<Review>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    void retry() {
        mMovieReviewsDataSourceFactory.retry();
    }

    void refreshReviewsList() {
        mMovieReviewsDataSourceFactory.getMovieReviewsDataSource().invalidate();
    }
}
