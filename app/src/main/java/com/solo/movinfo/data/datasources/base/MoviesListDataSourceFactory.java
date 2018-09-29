package com.solo.movinfo.data.datasources.base;


import android.arch.paging.DataSource;

import com.solo.movinfo.data.model.Movie;

public abstract class MoviesListDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    public abstract DataSource dataSource();

}
