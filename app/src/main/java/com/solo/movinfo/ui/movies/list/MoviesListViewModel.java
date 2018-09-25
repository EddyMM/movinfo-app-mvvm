package com.solo.movinfo.ui.movies.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.datasources.MoviesListDataSourceFactory;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.di.component.MoviesSubComponent;
import com.solo.movinfo.di.module.MoviesListModule;
import com.solo.movinfo.utils.Constants;

import javax.inject.Inject;

import timber.log.Timber;


public class MoviesListViewModel extends AndroidViewModel {
    @Inject
    MoviesListDataSourceFactory mMoviesListDataSourceFactory;

    private LiveData<PagedList<Movie>> mMoviesLiveData;

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

    LiveData<PagedList<Movie>> getMoviesLiveData() {
        return mMoviesLiveData;
    }

    private void fetchMoviesLiveData() {
        Timber.i("Loading movies from API");

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(Constants.MOVIES_LIST_PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build();

        mMoviesLiveData = new LivePagedListBuilder<>(mMoviesListDataSourceFactory, config)
                .build();
    }

    void refreshMoviesList() {
        mMoviesListDataSourceFactory.getMoviesListDataSource().invalidate();
    }

    void retry() {
        mMoviesListDataSourceFactory.getMoviesListDataSource().retry();
    }
}
