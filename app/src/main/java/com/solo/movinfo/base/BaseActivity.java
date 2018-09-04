package com.solo.movinfo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.di.component.ActivityComponent;
import com.solo.movinfo.di.component.DaggerActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(
                        ((MovinfoApplication) getApplication()).getApplicationComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

}
