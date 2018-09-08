package com.solo.movinfo.ui.movies.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.solo.movinfo.MovinfoApplication;
import com.solo.movinfo.R;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.network.models.Movie;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class MoviesListViewModel extends AndroidViewModel implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    DataManager mDataManager;
    MediatorLiveData<List<Movie>> mMoviesMediatorLiveData = new MediatorLiveData<>();

    public MoviesListViewModel(
            @NonNull Application application) {
        super(application);

        ((MovinfoApplication) application).getApplicationComponent().inject(this);

        fetchMoviesLiveData();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getApplication().getString(R.string.sort_order_key))) {
            fetchMoviesLiveData();
        }
    }

    private void fetchMoviesLiveData() {
        if (mDataManager.getSortCriteria().equals(
                getApplication().getString(R.string.sort_by_popularity_value))) {
            Timber.d("Loading popular movies from API");
            mMoviesMediatorLiveData.addSource(mDataManager.getPopularMovies(1),
                    movies -> mMoviesMediatorLiveData.setValue(movies));
        } else {
            Timber.d("Loading top rated movies from API");
            mMoviesMediatorLiveData.addSource(mDataManager.getTopRatedMovies(1),
                    movies -> mMoviesMediatorLiveData.setValue(movies));
        }
    }
}
