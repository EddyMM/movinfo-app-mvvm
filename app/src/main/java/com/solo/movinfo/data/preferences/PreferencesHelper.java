package com.solo.movinfo.data.preferences;

/**
 * Interface with abstract methods for getting preferences info
 */

public interface PreferencesHelper {

    void setSplashScreenSeen();

    boolean wasSplashScreenSeen();

    String getSortCriteria();
}
