package com.solo.movinfo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.datasources.base.PagedMoviesDataSource;
import com.solo.movinfo.data.datasources.popularmovies.PopularMoviesDataSourceFactory;
import com.solo.movinfo.data.datasources.topratedmovies.TopRatedMoviesDataSourceFactory;
import com.solo.movinfo.data.db.MoviesDatabase;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.di.component.MoviesSubComponent;
import com.solo.movinfo.di.module.MoviesListModule;
import com.solo.movinfo.utils.Constants;

import javax.inject.Inject;

import timber.log.Timber;


public class MoviesListViewModel extends AndroidViewModel {
    @Inject
    DataManager mDataManager;

    @Inject
    MoviesDatabase mMoviesDatabase;

    private PopularMoviesDataSourceFactory mPopularMoviesDataSourceFactory;
    private TopRatedMoviesDataSourceFactory mTopRatedMoviesDataSourceFactory;

    private LiveData<PagedList<Movie>> mPopularMoviesLiveData;
    private LiveData<PagedList<Movie>> mTopRatedMoviesLiveData;
    private LiveData<PagedList<Movie>> mFavoriteMoviesLiveData;

    public MoviesListViewModel(@NonNull Application application) {
        super(application);

        MoviesSubComponent moviesSubComponent = ((MovinfoApplication) application)
                .getApplicationComponent()
                .moviesSubComponentBuilder()
                .moviesListModule(new MoviesListModule())
                .build();

        moviesSubComponent.inject(this);
        fetchMoviesLiveData();
    }

    public LiveData<PagedList<Movie>> getPopularMoviesLiveData() {
        return mPopularMoviesLiveData;
    }

    public LiveData<PagedList<Movie>> getTopRatedMoviesLiveData() {
        return mTopRatedMoviesLiveData;
    }

    public LiveData<PagedList<Movie>> getFavoriteMoviesLiveData() {
        return mFavoriteMoviesLiveData;
    }

    private void fetchMoviesLiveData() {
        Timber.i("Loading movies from API");

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(Constants.MOVIES_LIST_PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build();

        mPopularMoviesDataSourceFactory = new PopularMoviesDataSourceFactory();
        mPopularMoviesLiveData = new LivePagedListBuilder<>(mPopularMoviesDataSourceFactory,
                config).build();

        mTopRatedMoviesDataSourceFactory = new TopRatedMoviesDataSourceFactory();
        mTopRatedMoviesLiveData = new LivePagedListBuilder<>(mTopRatedMoviesDataSourceFactory,
                config).build();

        DataSource.Factory<Integer, Movie> favoriteMoviesFactory =
                mMoviesDatabase.favoriteMovieModel().getFavoriteMovies();
        mFavoriteMoviesLiveData = new LivePagedListBuilder<>(favoriteMoviesFactory,
                config).build();

    }


    public void continueLoadingAfterInterruption() {
        if (mDataManager.getSortCriteria().equals(Constants.POPULARITY_PREFERENCE)) {
            ((PagedMoviesDataSource) mPopularMoviesDataSourceFactory.dataSource()).retry();
        } else if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            ((PagedMoviesDataSource) mTopRatedMoviesDataSourceFactory.dataSource()).retry();
        }
    }
}
