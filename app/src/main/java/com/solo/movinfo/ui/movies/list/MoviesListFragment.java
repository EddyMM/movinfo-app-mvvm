package com.solo.movinfo.ui.movies.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.solo.movinfo.utils.Constants;
import com.solo.movinfo.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Fragment to host and manage movies list
 */

public class MoviesListFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    DataManager mDataManager;

    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mMoviesListProgressBar;
    private MoviesListAdapter mMoviesListAdapter;
    private MoviesListViewModel moviesListViewModel;
    private Snackbar mInternetConnectionSnackbar;

    private boolean firstLoad;
    private ConnectivityStateChangeReceiver mConnectivityStateChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Ensure menu is displayed
        setHasOptionsMenu(true);

        ((MoviesListActivity) requireActivity()).getActivitySubComponent().inject(this);

        if (!mDataManager.wasSplashScreenSeen()) {
            mDataManager.setSplashScreenSeenByUser();
        }

        View view = inflater.inflate(R.layout.movies_list_fragment, container, false);

        initUI(view);

        // Read preferences and load UI accordingly
        setupSharedPreferences();

        setupViewModel();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerConnectivityChangeReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterConnectivityChangeReceiver();
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
        } else if ((item.getItemId() == R.id.movies_list_rating_menu_item)) {
            Timber.d("Sort by rating");
            mDataManager.setSortCriteria(Constants.RATING_PREFERENCE);
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
        moviesListViewModel.getMoviesLiveData().observe(this, movies -> {
            if (movies != null) {
                Timber.d("Adding list to adapter");
                mMoviesListAdapter.submitList(movies);
            } else {
                Timber.d("No movies fetched");
            }

            hideProgressBar();
            setMoviesListTitle();
            showList();
        });
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
        hideList();
        showProgressBar();

        moviesListViewModel.refreshMoviesList();
    }

    private void registerConnectivityChangeReceiver() {
        firstLoad = true;

        mConnectivityStateChangeReceiver = new ConnectivityStateChangeReceiver();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(mConnectivityStateChangeReceiver, intentFilter);
    }

    private void unregisterConnectivityChangeReceiver() {
        requireActivity().unregisterReceiver(mConnectivityStateChangeReceiver);
    }

    private class ConnectivityStateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = NetworkUtils.isInternetConnected(requireContext());

            if (!isConnected) {
                Timber.d("Not Connected!");
                showNotConnectedToInternet();
            }

            if (isConnected && !firstLoad) {
                Timber.d("Connected!");
                showConnectedToInternet();
            }

            firstLoad = false;
        }

        private void showConnectedToInternet() {
            if (mInternetConnectionSnackbar != null && mInternetConnectionSnackbar.isShown()) {
                mInternetConnectionSnackbar.dismiss();
            }

            mInternetConnectionSnackbar = Snackbar.make(
                    requireActivity().findViewById(R.id.single_fragment),
                    getString(R.string.connected_to_internet),
                    Snackbar.LENGTH_SHORT);

            mInternetConnectionSnackbar.getView()
                    .setBackgroundColor(getResources().getColor(R.color.connectedColor));

            mInternetConnectionSnackbar.show();

            List currentMoviesList = moviesListViewModel.getMoviesLiveData().getValue();
            if (currentMoviesList!= null && currentMoviesList.size() > 0) {
                moviesListViewModel.retry();
            } else {
                moviesListViewModel.refreshMoviesList();
            }
        }

        private void showNotConnectedToInternet() {
            if (mInternetConnectionSnackbar != null && mInternetConnectionSnackbar.isShown()) {
                mInternetConnectionSnackbar.dismiss();
            }

            mInternetConnectionSnackbar = Snackbar.make(
                    requireActivity().findViewById(R.id.single_fragment),
                    getString(R.string.no_internet_connection_message),
                    Snackbar.LENGTH_INDEFINITE);
            mInternetConnectionSnackbar.show();
        }
    }
}
