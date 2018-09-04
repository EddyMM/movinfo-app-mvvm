package com.solo.movinfo.ui.splash;

import android.support.v4.app.Fragment;

import com.solo.movinfo.base.SingleFragmentActivity;

public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SplashFragment();
    }
}
