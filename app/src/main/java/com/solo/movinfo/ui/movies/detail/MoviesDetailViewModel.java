package com.solo.movinfo.ui.movies.detail;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.datasources.MovieReviewsDataSourceFactory;
import com.solo.movinfo.data.model.Review;
import com.solo.movinfo.data.model.Video;
import com.solo.movinfo.data.network.responsemodels.VideosResponse;
import com.solo.movinfo.di.component.MoviesSubComponent;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class MoviesDetailViewModel extends ViewModel {

    @Inject
    DataManager mDataManager;
    private String mMovieId;

    private MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();
    private LiveData<PagedList<Review>> reviewsLiveData;

    private MovieReviewsDataSourceFactory mMovieReviewsDataSourceFactory;

    MoviesDetailViewModel(String id, Context context) {
        mMovieId = id;

        MoviesSubComponent moviesSubComponent = ((MovinfoApplication) context.getApplicationContext())
                .getApplicationComponent()
                .moviesSubComponentBuilder()
                .build();
        moviesSubComponent.inject(this);


        fetchReviews();
        fetchVideos();
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

    private void fetchVideos() {
        Callback<VideosResponse> videosResponseCallback = new Callback<VideosResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideosResponse> call,
                    @NonNull Response<VideosResponse> response) {
                if (!response.isSuccessful()) {
                    Timber.e(new HttpException(response));
                    return;
                }

                VideosResponse videosResponse = response.body();

                if (videosResponse != null) {
                    List<Video> videos = videosResponse.getVideos();
                    videosLiveData.postValue(videos);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosResponse> call, @NonNull Throwable t) {
                Timber.e(t);
            }
        };

        mDataManager.getVideos(mMovieId, videosResponseCallback);
    }

    LiveData<List<Video>> getVideosLiveData() {
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
