package com.solo.movinfo.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import com.solo.movinfo.utils.AppExecutors;
import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.db.MoviesDatabase;
import com.solo.movinfo.data.datasources.reviews.MovieReviewsDataSourceFactory;
import com.solo.movinfo.data.model.Movie;
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
    private int mMovieId;

    private MutableLiveData<List<Video>> mVideosLiveData = new MutableLiveData<>();
    private LiveData<PagedList<Review>> mReviewsLiveData;
    private LiveData<Movie> mCurrentFavoriteMovieLiveData;

    private MovieReviewsDataSourceFactory mMovieReviewsDataSourceFactory;

    MoviesDetailViewModel(int id, Context context) {
        mMovieId = id;

        MoviesSubComponent moviesSubComponent =
                ((MovinfoApplication) context.getApplicationContext())
                        .getApplicationComponent()
                        .moviesSubComponentBuilder()
                        .build();
        moviesSubComponent.inject(this);


        fetchReviews();
        fetchVideos();
        fetchFavoriteMovie(context);
    }

    private void fetchFavoriteMovie(Context context) {
        mCurrentFavoriteMovieLiveData = MoviesDatabase.getInstance(context)
                .favoriteMovieModel()
                .getFavoriteMovieById(mMovieId);
    }

    private void fetchReviews() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        mMovieReviewsDataSourceFactory =
                new MovieReviewsDataSourceFactory(mMovieId);

        mReviewsLiveData = new LivePagedListBuilder<>(mMovieReviewsDataSourceFactory, config)
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
                    mVideosLiveData.postValue(videos);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosResponse> call, @NonNull Throwable t) {
                Timber.e(t);
            }
        };

        mDataManager.loadVideos(mMovieId, videosResponseCallback);
    }

    public LiveData<List<Video>> getVideosLiveData() {
        return mVideosLiveData;
    }

    public LiveData<PagedList<Review>> getReviewsLiveData() {
        return mReviewsLiveData;
    }

    public LiveData<Movie> getCurrentFavoriteMovieLiveData() {
        return mCurrentFavoriteMovieLiveData;
    }

    public void retry() {
        mMovieReviewsDataSourceFactory.retry();
    }

    public void refreshReviewsList() {
        DataSource reviewsDataSource = mMovieReviewsDataSourceFactory.getMovieReviewsDataSource();
        if (reviewsDataSource != null) {
            reviewsDataSource.invalidate();
        }
    }


    public void addOrRemoveFromFavorites(Context context, Movie movie) {
        if (mCurrentFavoriteMovieLiveData.getValue() == null) {
            AppExecutors.getInstance().diskIO().execute(() ->
                    MoviesDatabase.getInstance(context)
                            .favoriteMovieModel()
                            .addFavoriteMovie(movie)
            );
        } else {
            AppExecutors.getInstance().diskIO().execute(() ->
                    MoviesDatabase.getInstance(context)
                            .favoriteMovieModel()
                            .removeFavoriteMovie(movie)
            );
        }
    }
}
