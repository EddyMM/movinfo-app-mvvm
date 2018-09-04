package com.solo.movinfo.ui.movies.list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.solo.movinfo.base.SingleFragmentActivity;


/**
 * Activity to manage the list of movies
 */

public class MoviesListActivity extends SingleFragmentActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, MoviesListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new MoviesListFragment();
    }
}
