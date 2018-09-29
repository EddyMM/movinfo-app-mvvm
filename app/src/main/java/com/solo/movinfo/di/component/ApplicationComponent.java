package com.solo.movinfo.di.component;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.preferences.PreferencesHelper;
import com.solo.movinfo.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MovinfoApplication app);

    void inject(PreferencesHelper preferencesHelper);

    MoviesSubComponent.Builder moviesSubComponentBuilder();

    ActivitySubComponent.Builder activitySubComponentBuilder();
}
