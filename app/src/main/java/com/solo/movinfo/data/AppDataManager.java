package com.solo.movinfo.data;


import com.solo.movinfo.data.preferences.PreferencesHelper;

import javax.inject.Inject;

public class AppDataManager implements DataManager {

    private PreferencesHelper mAppPreferencesHelper;

    @Inject
    AppDataManager(PreferencesHelper appPreferencesHelper) {
        mAppPreferencesHelper = appPreferencesHelper;
    }

    @Override
    public void setSplashScreenSeenByUser() {
        mAppPreferencesHelper.setSplashScreenSeen();
    }

    @Override
    public boolean wasSplashScreenSeen() {
        return mAppPreferencesHelper.wasSplashScreenSeen();
    }

    @Override
    public void setSortCriteria(String sortCriteria) {
        mAppPreferencesHelper.setSortCriteria(sortCriteria);
    }

    @Override
    public String getSortCriteria() {
        return mAppPreferencesHelper.getSortCriteria();
    }
}
