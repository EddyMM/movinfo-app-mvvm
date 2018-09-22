package com.solo.movinfo.data;


import android.arch.lifecycle.LiveData;

import com.solo.movinfo.data.network.models.Movie;

import java.util.List;

public interface DataManager {

    void setSplashScreenSeenByUser();

    boolean wasSplashScreenSeen();

    void setSortCriteria(String sortCriteria);

    String getSortCriteria();

}
