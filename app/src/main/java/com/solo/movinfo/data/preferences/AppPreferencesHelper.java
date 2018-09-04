package com.solo.movinfo.data.preferences;

import static com.solo.movinfo.utils.Constants.SPLASH_SCREEN_SEEN;

import android.content.Context;
import android.content.SharedPreferences;

import com.solo.movinfo.R;

import javax.inject.Inject;

/**
 * Helper class for storing and retrieving info from the shared preferences
 */

public class AppPreferencesHelper implements PreferencesHelper {

    SharedPreferences mSharedPreferences;
    Context mContext;

    @Inject
    public AppPreferencesHelper(SharedPreferences sharedPreferences,
            Context context) {
        mSharedPreferences = sharedPreferences;
        mContext = context;
    }

    @Override
    public void setSplashScreenSeen() {
        mSharedPreferences
                .edit()
                .putBoolean(SPLASH_SCREEN_SEEN, true)
                .apply();
    }

    @Override
    public boolean wasSplashScreenSeen() {
        return mSharedPreferences.getBoolean(SPLASH_SCREEN_SEEN, false);
    }

    @Override
    public String getSortCriteria() {
        return mSharedPreferences.getString(mContext.getString(R.string.sort_order_key),
                mContext.getString(R.string.sort_by_popularity_value));
    }
}
