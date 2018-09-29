package com.solo.movinfo.di.module;

import android.content.Context;

import com.solo.movinfo.data.db.MoviesDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesListModule {

    @Provides
    MoviesDatabase provideMoviesDatabase(Context context) {
        return MoviesDatabase.getInstance(context);
    }

}
