package com.solo.movinfo.di.component;


import com.solo.movinfo.di.module.ActivityModule;
import com.solo.movinfo.ui.movies.list.MoviesListFragment;
import com.solo.movinfo.ui.splash.SplashFragment;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivitySubComponent {

    @Subcomponent.Builder
    interface Builder {
        ActivitySubComponent.Builder activityModule(ActivityModule activityModule);

        ActivitySubComponent build();
    }

    void inject(MoviesListFragment moviesListFragment);

    void inject(SplashFragment splashFragment);

}
