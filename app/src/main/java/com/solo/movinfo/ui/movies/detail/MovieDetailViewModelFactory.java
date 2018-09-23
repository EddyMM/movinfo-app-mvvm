package com.solo.movinfo.ui.movies.detail;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String mMovieId;

    public MovieDetailViewModelFactory(String id) {
        mMovieId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MoviesDetailViewModel(mMovieId);
    }
}
