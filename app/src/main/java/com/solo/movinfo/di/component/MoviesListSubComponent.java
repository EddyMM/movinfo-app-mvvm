package com.solo.movinfo.di.component;


import com.solo.movinfo.di.module.MoviesListModule;
import com.solo.movinfo.ui.movies.list.MoviesListViewModel;

import dagger.Subcomponent;

@Subcomponent(modules = MoviesListModule.class)
public interface MoviesListSubComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder moviesListModule(MoviesListModule moviesListModule);
        MoviesListSubComponent build();
    }

    void inject(MoviesListViewModel moviesListViewModel);

}
