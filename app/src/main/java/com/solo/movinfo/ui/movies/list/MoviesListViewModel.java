package com.solo.movinfo.ui.movies.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.datasources.movieslist.MoviesListDataSourceFactory;
import com.solo.movinfo.data.network.models.Movie;
import com.solo.movinfo.di.component.MoviesListSubComponent;
import com.solo.movinfo.di.module.MoviesListModule;

import javax.inject.Inject;

import timber.log.Timber;


public class MoviesListViewModel extends AndroidViewModel {
    @Inject
    MoviesListDataSourceFactory mMoviesListDataSourceFactory;

    private LiveData<PagedList<Movie>> mMoviesLiveData;

    LiveData<PagedList<Movie>> getMoviesLiveData() {
        return mMoviesLiveData;
    }


    public MoviesListViewModel(
            @NonNull Application application) {
        super(application);

        MoviesListSubComponent moviesListSubComponent = ((MovinfoApplication) application)
                .getApplicationComponent()
                .moviesListSubComponentBuilder()
                .moviesListModule(new MoviesListModule())
                .build();

        moviesListSubComponent.inject(this);

        fetchMoviesLiveData();
    }

    private void fetchMoviesLiveData() {
        Timber.d("Loading movies from API");

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .build();

        mMoviesLiveData = new LivePagedListBuilder<>(mMoviesListDataSourceFactory, config)
                .build();
    }

    void refreshMoviesList() {
        mMoviesListDataSourceFactory.getMoviesListDataSource().invalidate();
    }
}
