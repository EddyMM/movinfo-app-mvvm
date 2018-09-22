package com.solo.movinfo.ui.movies.list;

import android.support.v4.app.Fragment;

import com.solo.movinfo.base.SingleFragmentActivity;


/**
 * Activity to manage the list of movies
 */

public class MoviesListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MoviesListFragment();
    }

}
