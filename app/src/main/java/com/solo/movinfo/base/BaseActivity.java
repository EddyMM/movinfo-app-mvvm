package com.solo.movinfo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.di.component.ActivitySubComponent;
import com.solo.movinfo.di.module.ActivityModule;


public abstract class BaseActivity extends AppCompatActivity {

    private ActivitySubComponent mActivitySubComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mActivitySubComponent = ((MovinfoApplication) getApplication())
                .getApplicationComponent()
                .activitySubComponentBuilder()
                .activityModule(new ActivityModule())
                .build();
    }

    public ActivitySubComponent getActivitySubComponent() {
        return mActivitySubComponent;
    }

}
