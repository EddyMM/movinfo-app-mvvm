package com.solo.movinfo.ui.movies.detail;

import android.support.v4.app.Fragment;

import com.solo.movinfo.base.SingleFragmentActivity;

public class MoviesDetailActivity extends SingleFragmentActivity {
    public static final String MOVIE_INTENT_EXTRA = "MOVIE_INTENT_EXTRA";

    @Override
    protected Fragment createFragment() {
        return new MoviesDetailFragment();
    }
}
