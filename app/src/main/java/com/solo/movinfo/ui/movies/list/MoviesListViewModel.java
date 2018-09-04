package com.solo.movinfo.ui.movies.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.R;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.network.models.MoviesResponse;

import javax.inject.Inject;

import timber.log.Timber;


public class MoviesListViewModel extends AndroidViewModel {
    @Inject
    DataManager mDataManager;
    LiveData<MoviesResponse> movies;

    public MoviesListViewModel(
            @NonNull Application application) {
        super(application);

        ((MovinfoApplication) application).getApplicationComponent().inject(this);

        if (mDataManager.getSortCriteria().equals(
                getApplication().getString(R.string.sort_by_popularity_value))) {
            Timber.d("Loading popular movies from API");
            movies = mDataManager.getPopularMovies(1);
        }
    }
}
