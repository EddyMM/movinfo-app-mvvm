package com.solo.movinfo.data;


public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    void setSortCriteria(String sortCriteria);

    String getSortCriteria();

}
