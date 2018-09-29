package com.solo.movinfo.data.datasources.popularmovies;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.datasources.base.MoviesListDataSourceFactory;
import com.solo.movinfo.data.model.Movie;

public class PopularMoviesDataSourceFactory extends MoviesListDataSourceFactory {

    private PopularMoviesDataSource mPopularMoviesDataSource;

    @Override
    public DataSource<Integer, Movie> create() {
        mPopularMoviesDataSource = new PopularMoviesDataSource();
        return mPopularMoviesDataSource;
    }

    @Override
    public DataSource dataSource() {
        return mPopularMoviesDataSource;
    }
}
