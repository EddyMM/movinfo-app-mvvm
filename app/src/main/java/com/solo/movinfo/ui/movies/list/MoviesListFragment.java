package com.solo.movinfo.ui.movies.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.solo.movinfo.R;
import com.solo.movinfo.base.InternetAwareFragment;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Fragment to host and manage movies list
 */

public class MoviesListFragment extends InternetAwareFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    DataManager mDataManager;

    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mMoviesListProgressBar;
    private MoviesListAdapter mMoviesListAdapter;

    private MoviesListViewModel moviesListViewModel;

    private MoviesListObserver mMoviesListObserver = new MoviesListObserver();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Ensure menu is displayed
        setHasOptionsMenu(true);

        ((MoviesListActivity) requireActivity()).getActivitySubComponent().inject(this);

        // Ensure user sees splash screen once per install after opening the movies list
        if (!mDataManager.wasSplashScreenSeen()) {
            mDataManager.setSplashScreenSeenByUser();
        }

        View view = inflater.inflate(R.layout.movies_list_fragment, container, false);

        initUI(view);

        // Register as listener for any changes in shared preferences
        setupSharedPreferences();

        setupViewModel();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(requireContext());

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_list_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movies_list_popularity_menu_item) {
            Timber.d("Sort by popularity");
            mDataManager.setSortCriteria(Constants.POPULARITY_PREFERENCE);
            return true;
        } else if (item.getItemId() == R.id.movies_list_rating_menu_item) {
            Timber.d("Sort by rating");
            mDataManager.setSortCriteria(Constants.RATING_PREFERENCE);
            return true;
        } else if (item.getItemId() == R.id.movies_list_favorites_menu_item) {
            Timber.d("Sort by favorites");
            mDataManager.setSortCriteria(Constants.FAVORITES_PREFERENCE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeAllObservers() {
        moviesListViewModel.getFavoriteMoviesLiveData().removeObservers(this);
        moviesListViewModel.getPopularMoviesLiveData().removeObservers(this);
        moviesListViewModel.getTopRatedMoviesLiveData().removeObservers(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_order_key))) {
            Timber.d("Detected a change in preferences to: %s", mDataManager.getSortCriteria());
            refresh();
        }
    }

    private void initUI(View view) {
        mMoviesRecyclerView = view.findViewById(R.id.moviesListRecyclerView);

        GridLayoutManager moviesListGridLayout = new GridLayoutManager(this.getContext(),
                Constants.MOVIES_LIST_NO_OF_COLUMNS);

        mMoviesRecyclerView.setLayoutManager(moviesListGridLayout);
        mMoviesListAdapter = new MoviesListAdapter(this.getContext());
        mMoviesRecyclerView.setAdapter(mMoviesListAdapter);

        mMoviesListProgressBar = view.findViewById(R.id.moviesListProgressBar);

        setMoviesListTitle();
    }

    private void setupViewModel() {
        showProgressBar();

        moviesListViewModel = ViewModelProviders.of(this).get(
                MoviesListViewModel.class);

        attachMoviesListLiveData();
    }

    private void attachMoviesListLiveData() {
        if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            moviesListViewModel.getTopRatedMoviesLiveData().observe(this, mMoviesListObserver);
        } else if (mDataManager.getSortCriteria().equals(Constants.FAVORITES_PREFERENCE)) {
            moviesListViewModel.getFavoriteMoviesLiveData().observe(this, mMoviesListObserver);
        } else {
            moviesListViewModel.getPopularMoviesLiveData().observe(this, mMoviesListObserver);
        }
    }

    /**
     * Load preferences and register as a listener for any changes in preferences
     */
    private void setupSharedPreferences() {
        // Load preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                this.requireContext());

        // Register as a listener for any changes in preferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setMoviesListTitle() {
        if (mDataManager.getSortCriteria().equals(Constants.RATING_PREFERENCE)) {
            requireActivity().setTitle(getString(R.string.top_rated_movies_title));
        } else if (mDataManager.getSortCriteria().equals(Constants.FAVORITES_PREFERENCE)) {
            requireActivity().setTitle(getString(R.string.favorite_movies_title));
        } else {
            requireActivity().setTitle(getString(R.string.popular_movies_title));
        }
    }

    public void showProgressBar() {
        mMoviesListProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mMoviesListProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showList() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideList() {
        mMoviesRecyclerView.setVisibility(View.GONE);
    }

    private void refresh() {
        removeAllObservers();

        hideList();
        showProgressBar();

        attachMoviesListLiveData();
    }

    @Override
    protected void onInternetConnectivityOff() {}

    /**
     * If part of the list was already loaded, it attempts to continue loading the rest
     * If movies list was not loaded at all, it attempts to fetch the whole list
     */
    @Override
    protected void onInternetConnectivityOn() {
        List currentMoviesList = mMoviesListAdapter.getCurrentList();
        if (currentMoviesList != null && currentMoviesList.size() > 0) {
            Timber.d("Retrying movies list fetch");
            moviesListViewModel.continueLoadingAfterInterruption();
        } else {
            refresh();
        }
    }

    class MoviesListObserver implements Observer<PagedList<Movie>> {
        @Override
        public void onChanged(@Nullable PagedList<Movie> movies) {
            if (movies != null) {
                Timber.i("Adding movies list to adapter");
                mMoviesListAdapter.submitList(movies);
            } else {
                Timber.i("No movies fetched");
            }

            hideProgressBar();
            setMoviesListTitle();
            showList();
        }
    }
}
