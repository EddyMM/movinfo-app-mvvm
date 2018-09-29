package com.solo.movinfo.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.data.AppDataManager;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.preferences.AppPreferencesHelper;
import com.solo.movinfo.data.preferences.PreferencesHelper;
import com.solo.movinfo.di.component.ActivitySubComponent;
import com.solo.movinfo.di.component.MoviesSubComponent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(subcomponents = {MoviesSubComponent.class, ActivitySubComponent.class})
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    MovinfoApplication provideApplication() {
        return (MovinfoApplication) mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }
}
