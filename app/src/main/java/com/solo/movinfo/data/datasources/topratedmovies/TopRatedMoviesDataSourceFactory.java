package com.solo.movinfo.data.datasources.topratedmovies;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.datasources.base.MoviesListDataSourceFactory;
import com.solo.movinfo.data.model.Movie;

public class TopRatedMoviesDataSourceFactory extends MoviesListDataSourceFactory {

    private TopRatedMoviesDataSource mTopRatedMoviesDataSource;

    @Override
    public DataSource<Integer, Movie> create() {
        mTopRatedMoviesDataSource = new TopRatedMoviesDataSource();
        return mTopRatedMoviesDataSource;
    }

    @Override
    public DataSource dataSource() {
        return mTopRatedMoviesDataSource;
    }
}
