package com.solo.movinfo.di.module;

import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.datasources.MoviesListDataSourceFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesListModule {

    @Provides
    MoviesListDataSourceFactory provideMoviesListDataSourceFactory(DataManager dataManager) {
        return new MoviesListDataSourceFactory(dataManager);
    }

}
