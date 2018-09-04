package com.solo.movinfo.di.component;

import com.solo.movinfo.di.PerActivity;
import com.solo.movinfo.di.module.ActivityModule;
import com.solo.movinfo.ui.movies.list.MoviesListFragment;
import com.solo.movinfo.ui.splash.SplashFragment;


import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashFragment splashFragment);

    void inject(MoviesListFragment moviesListFragment);
}
