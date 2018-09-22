package com.solo.movinfo.data.datasources.movieslist;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.network.models.Movie;

public class MoviesListDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MoviesListDataSource mMoviesListDataSource;
    private DataManager mDataManager;

    private MutableLiveData<MoviesListDataSource>
            mMoviesDataSourceMutableLiveData = new MutableLiveData<>();

    public MoviesListDataSourceFactory(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public MoviesListDataSource getMoviesListDataSource() {
        return mMoviesListDataSource;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        mMoviesListDataSource = new MoviesListDataSource(mDataManager);
        mMoviesDataSourceMutableLiveData.postValue(mMoviesListDataSource);
        return mMoviesListDataSource;
    }
}
