package com.solo.movinfo.ui.movies.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.solo.movinfo.BuildConfig;
import com.solo.movinfo.R;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.data.model.Review;
import com.solo.movinfo.utils.Constants;
import com.solo.movinfo.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

/**
 * @author eddy.
 */

public class MoviesDetailFragment extends Fragment {
    private static final String TAG = MoviesDetailFragment.class.getSimpleName();

    private ActionBar mActionBar;
    private Movie mMovie;

    private MoviesDetailViewModel mMoviesDetailViewModel;
    private ReviewsAdapter reviewsAdapter;
    private Snackbar mInternetConnectionSnackbar;

    private boolean firstLoad; // Used by connectivity receiver to avoid taking actions
    // if user is initially connected

    private ConnectivityStateChangeReceiver mConnectivityStateChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = requireActivity().getIntent();

        mMovie = null;
        if (intent != null) {
            mMovie = intent.getParcelableExtra(MoviesDetailActivity.MOVIE_INTENT_EXTRA);
        }

        View v = inflater.inflate(R.layout.movies_detail_fragment, container, false);
        setupUI(v, mMovie);

        setupViewModel();

        return v;
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

    private void setupViewModel() {
        if (mMovie != null) {
            MovieDetailViewModelFactory movieDetailViewModelFactory =
                    new MovieDetailViewModelFactory(
                            mMovie.getMovieId());

            mMoviesDetailViewModel = ViewModelProviders.of(this, movieDetailViewModelFactory)
                    .get(MoviesDetailViewModel.class);

            LiveData<PagedList<Review>> reviewsLiveData =
                    mMoviesDetailViewModel.getReviewsLiveData();

            reviewsLiveData.observe(this, (reviews) -> {
                Timber.d("Reviews: %s", reviews);
                reviewsAdapter.submitList(reviews);
            });
        }
    }

    /**
     * Binds UI with the mMovie data
     */
    private void setupUI(View v, Movie movie) {
        TextView movieTitleTextView = v.findViewById(R.id.movieDetailsTitleTextView);
        TextView movieSynopsisTextView = v.findViewById(R.id.movieSynopsisTextView);
        TextView releaseDateTextView = v.findViewById(R.id.releaseDateTextView);
        TextView voteAverageTextView = v.findViewById(R.id.voteAverageTextView);

        ImageView moviePosterImageView = v.findViewById(R.id.movieDetailsPosterImageView);
        RatingBar voteAverageBar = v.findViewById(R.id.movieDetailsRatingBar);

        RecyclerView reviewsRecyclerView = v.findViewById(R.id.reviewsRecyclerView);

        if (movie != null) {
            Log.d(TAG, "Showing mMovie: " + movie);
            // Display poster
            String posterPath =
                    Constants.MOVIE_DB_POSTER_URL + Constants.MOVIES_DETAILS_POSTER_RESOLUTION
                            + movie.getPosterPath()
                            + "?api_key=" + BuildConfig.TheMovieDbApiToken;
            Picasso.get().load(posterPath)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(moviePosterImageView);

            // Title
            movieSynopsisTextView.setText(movie.getOverview());

            // Rating
            float rating = 5 * (movie.getVoteAverage() / 10);
            voteAverageBar.setRating(rating);

            // Vote average
            voteAverageTextView.setText(String.valueOf(movie.getVoteAverage()));


            // Release date
            releaseDateTextView.setText(movie.getFormattedDate());

            if (mActionBar != null) {
                mActionBar.setTitle(movie.getTitle());
                movieTitleTextView.setText(movie.getTitle());
            }

            // Attach an adapter
            reviewsAdapter = new ReviewsAdapter(requireContext());
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        } else {
            Log.w(TAG, "Movie in Details screen is null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            // Return to the movies list
            NavUtils.navigateUpFromSameTask(requireActivity());
        }

        return super.onOptionsItemSelected(item);
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

            // Retry if initial load was done but otherwise fetch whole e.g. if on app start
            // device was disconnected but user established a connection
            List currentReviewsList = mMoviesDetailViewModel.getReviewsLiveData().getValue();
            if (currentReviewsList != null && currentReviewsList.size() > 0) {
                mMoviesDetailViewModel.retry();
            } else {
                mMoviesDetailViewModel.refreshReviewsList();
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
