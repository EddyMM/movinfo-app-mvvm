package com.solo.movinfo.di.component;


import com.solo.movinfo.di.module.MoviesListModule;
import com.solo.movinfo.ui.movies.detail.MoviesDetailViewModel;
import com.solo.movinfo.ui.movies.list.MoviesListViewModel;

import dagger.Subcomponent;

@Subcomponent(modules = MoviesListModule.class)
public interface MoviesSubComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder moviesListModule(MoviesListModule moviesListModule);
        MoviesSubComponent build();
    }

    void inject(MoviesListViewModel moviesListViewModel);

    void inject(MoviesDetailViewModel moviesListViewModel);

}
