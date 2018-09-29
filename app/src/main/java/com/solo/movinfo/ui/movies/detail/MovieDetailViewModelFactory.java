package com.solo.movinfo.ui.movies.detail;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private int mMovieId;
    private Context mContext;

    public MovieDetailViewModelFactory(int id, Context context) {
        mMovieId = id;
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MoviesDetailViewModel(mMovieId, mContext);
    }
}
