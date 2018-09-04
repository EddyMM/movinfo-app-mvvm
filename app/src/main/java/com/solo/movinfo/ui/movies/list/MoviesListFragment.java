package com.solo.movinfo.ui.movies.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.ui.movies.settings.SettingsActivity;
import com.solo.movinfo.utils.Constants;
import com.solo.movinfo.utils.NetworkUtils;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Fragment to host and manage movies list
 */

public class MoviesListFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    DataManager mDataManager;

    private ProgressBar mMoviesListProgressBar;
    private MoviesListAdapter mMoviesListAdapter;
    private GridLayoutManager mMoviesListGridLayout;
    private Snackbar mInternetConnectionSnackbar;
    private boolean isLoading;
    private boolean retryAttempted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Ensure menu is displayed
        setHasOptionsMenu(true);

        ((MoviesListActivity) requireActivity()).getActivityComponent().inject(this);
        // mMoviesListPresenter.onAttach(this);

        if (!mDataManager.wasSplashScreenSeen()) {
            mDataManager.setSplashScreenSeenByUser();
        }

        View view = inflater.inflate(R.layout.movies_list_fragment, container, false);

        initUI(view);

        setupViewModel();

        // Read preferences and load UI accordingly
        setupSharedPreferences();

        return view;
    }

    private void setupViewModel() {
        MoviesListViewModel moviesListViewModel = ViewModelProviders.of(this).get(
                MoviesListViewModel.class);

        showProgressBar();

        moviesListViewModel.movies.observe(this, moviesResponse -> {
            if (moviesResponse != null) {
                mMoviesListAdapter.addMovies(moviesResponse.getResults());
            } else {
                Timber.d("No movies fetched");
            }

            hideProgressBar();
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_list_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movies_list_settings_menu_item) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(requireContext());

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initUI(View view) {
        RecyclerView moviesRecyclerView = view.findViewById(R.id.moviesListRecyclerView);

        mMoviesListGridLayout = new GridLayoutManager(this.getContext(),
                Constants.MOVIES_LIST_NO_OF_COLUMNS);

        moviesRecyclerView.setLayoutManager(mMoviesListGridLayout);
        mMoviesListAdapter = new MoviesListAdapter(this.getContext());
        moviesRecyclerView.setAdapter(mMoviesListAdapter);
        moviesRecyclerView.addOnScrollListener(new MoviesListScrollListener());

        mMoviesListProgressBar = view.findViewById(R.id.moviesListProgressBar);
    }

    // @Override
    public void showProgressBar() {
        mMoviesListProgressBar.setVisibility(View.VISIBLE);
    }

    // @Override
    public void hideProgressBar() {
        mMoviesListProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Displays a SnackBar prompting user to connect to the internet in order to load movies
     */
    // @Override
    public void showNoInternetConnectionMessage() {
        if (mInternetConnectionSnackbar == null || retryAttempted) {
            // Reset the retry attempt to ensure recursive retries are recognized
            retryAttempted = false;

            mInternetConnectionSnackbar = Snackbar.make(
                    requireActivity().findViewById(R.id.single_fragment),
                    getString(R.string.no_internet_connection_message), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry), (v) -> {
                        if (!NetworkUtils.isInternetConnected(requireContext())) {
                            retryAttempted = true;
                            showNoInternetConnectionMessage();
                            return;
                        }
                        fetchNextPageOfMovies();
                    });
        }

        if (!mInternetConnectionSnackbar.isShown()) {
            mInternetConnectionSnackbar.show();
        }
    }

    /**
     * Removes the SnackBar prompting user to connect to the internet
     */
    // @Override
    public void removeNoInternetConnectionMessage() {
        if (mInternetConnectionSnackbar != null && mInternetConnectionSnackbar.isShown()) {
            mInternetConnectionSnackbar.dismiss();
        }
    }

    // @Override
    public void setIsLoadingMovies(boolean isLoadingMovies) {
        isLoading = isLoadingMovies;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_order_key))) {
            fetchNextPageOfMovies();
        }
    }

    public void openSettings() {
        Intent settingsIntent = new Intent(requireActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /**
     * Load preferences and register as a listener for any changes in preferences
     */
    private void setupSharedPreferences() {
        // Load preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                this.requireContext());

        // Go ahead and fetch movies after reading preferred sort criteria
        fetchNextPageOfMovies();

        // Register as a listener for any changes in preferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Gets the next page of movies based on the movies sort criteria
     */
    private void fetchNextPageOfMovies() {
        setIsLoadingMovies(true);

        // Ensure device is connected to the internet
        if (!NetworkUtils.isInternetConnected(requireContext())) {
            setIsLoadingMovies(false);
            showNoInternetConnectionMessage();
            return;
        }

        // Dismiss the no internet connection if it is shown
        removeNoInternetConnectionMessage();

        // mMoviesListPresenter.onFetchMovies();
    }

    /**
     * Scroll Listener to implement pagination (fetch more movies as the
     * user scrolls through them)
     */
    private class MoviesListScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // Obtain some info about the number of items displayed already
            int visibleItemCount = mMoviesListGridLayout.getChildCount();
            int totalItemCount = mMoviesListGridLayout.getItemCount();
            int lastVisibleItemPosition = mMoviesListGridLayout.findLastVisibleItemPosition();

            Timber.d("Total item count: %s, Visible item count: %s, Last visible item pos: %s",
                    totalItemCount, visibleItemCount, lastVisibleItemPosition);

            // Apply pagination only if movies are not already being fetched as scrolling happens
            if (!isLoading) {
                if (lastVisibleItemPosition >= (totalItemCount * Constants.SCROLL_PAGINATION_RATIO)
                        && visibleItemCount < totalItemCount) {
                    Timber.d("Fetching more movies");
                    fetchNextPageOfMovies();
                }
            }
        }
    }
}
