package com.solo.movinfo;

import android.app.Application;

import com.solo.movinfo.di.component.ApplicationComponent;
import com.solo.movinfo.di.component.DaggerApplicationComponent;
import com.solo.movinfo.di.module.ApplicationModule;

import timber.log.Timber;

public class MovinfoApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initTimberLogging();

        // Interface for dependency injection
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mApplicationComponent.inject(this);
    }

    private void initTimberLogging() {
        Timber.plant(new Timber.DebugTree());
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
