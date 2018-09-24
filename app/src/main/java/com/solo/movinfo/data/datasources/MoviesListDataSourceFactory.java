package com.solo.movinfo.data.datasources;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.model.Movie;

public class MoviesListDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MoviesListDataSource mMoviesListDataSource;
    private DataManager mDataManager;


    public MoviesListDataSourceFactory(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public MoviesListDataSource getMoviesListDataSource() {
        return mMoviesListDataSource;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        mMoviesListDataSource = new MoviesListDataSource(mDataManager);

        return mMoviesListDataSource;
    }
}
