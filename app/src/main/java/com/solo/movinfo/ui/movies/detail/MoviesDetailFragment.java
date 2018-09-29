package com.solo.movinfo.ui.movies.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.solo.movinfo.AppExecutors;
import com.solo.movinfo.BuildConfig;
import com.solo.movinfo.R;
import com.solo.movinfo.base.InternetAwareFragment;
import com.solo.movinfo.data.db.MoviesDatabase;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.data.model.Review;
import com.solo.movinfo.data.model.Video;
import com.solo.movinfo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

/**
 * Fragment to manage details about a specific movie
 */

public class MoviesDetailFragment extends InternetAwareFragment {
    private static final String TAG = MoviesDetailFragment.class.getSimpleName();

    private ActionBar mActionBar;
    private Movie mMovie;

    private MoviesDetailViewModel mMoviesDetailViewModel;
    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;
    private TextView mReviewsLabelTextView;
    private TextView mVideosLabelTextView;
    private ImageView mImageView;

    private ProgressBar mMovieDetailsProgressBar;

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
        initUI(v, mMovie);

        setupViewModel();

        return v;
    }

    private void setupViewModel() {
        if (mMovie != null) {
            MovieDetailViewModelFactory movieDetailViewModelFactory =
                    new MovieDetailViewModelFactory(mMovie.getMovieId(), requireContext());

            mMoviesDetailViewModel = ViewModelProviders.of(this, movieDetailViewModelFactory)
                    .get(MoviesDetailViewModel.class);


            LiveData<PagedList<Review>> reviewsLiveData =
                    mMoviesDetailViewModel.getReviewsLiveData();

            showProgressBar();

            LiveData<Movie> favoriteMovieLiveData =
                    mMoviesDetailViewModel.getCurrentFavoriteMovieLiveData();
            favoriteMovieLiveData.observe(this, (movie) -> {
                Timber.d("isInFavorite movie: %s", movie);
                if (movie != null) {
                    setFavoriteOn();
                } else {
                    setFavoriteOff();
                }
            });

            reviewsLiveData.observe(this, (reviews) -> {
                hideProgressBar();

                if (reviews != null && reviews.size() > 0) {
                    mReviewsLabelTextView.setVisibility(View.VISIBLE);
                    Timber.d("Reviews: %s", reviews);
                    mReviewsAdapter.submitList(reviews);
                }
            });

            LiveData<List<Video>> videosLiveData = mMoviesDetailViewModel.getVideosLiveData();
            videosLiveData.observe(this, (videos) -> {
                if (videos != null && videos.size() > 0) {
                    mVideosLabelTextView.setVisibility(View.VISIBLE);
                    mVideosAdapter.submitVideos(videos);
                    Timber.d("Videos: %s", videos);
                }
            });
        }
    }

    /**
     * Binds UI with the mMovie data
     */
    private void initUI(View v, Movie movie) {
        TextView movieTitleTextView = v.findViewById(R.id.movieDetailsTitleTextView);
        TextView movieSynopsisTextView = v.findViewById(R.id.movieSynopsisTextView);
        TextView releaseDateTextView = v.findViewById(R.id.releaseDateTextView);
        TextView voteAverageTextView = v.findViewById(R.id.voteAverageTextView);
        mReviewsLabelTextView = v.findViewById(R.id.reviewsLabelTextView);
        mVideosLabelTextView = v.findViewById(R.id.videosLabelTextView);
        mMovieDetailsProgressBar = v.findViewById(R.id.movieDetailsProgressBar);

        ImageView moviePosterImageView = v.findViewById(R.id.movieDetailsPosterImageView);
        RatingBar voteAverageBar = v.findViewById(R.id.movieDetailsRatingBar);

        RecyclerView reviewsRecyclerView = v.findViewById(R.id.reviewsRecyclerView);
        RecyclerView videosRecyclerView = v.findViewById(R.id.videosRecyclerView);

        if (movie != null) {
            Log.d(TAG, "Showing mMovie: " + movie);
            // Display poster
            String posterPath =
                    Constants.MOVIE_DB_POSTER_URL + Constants.MOVIES_DETAILS_POSTER_RESOLUTION
                            + movie.getPosterPath()
                            + "?api_key=" + BuildConfig.TheMovieDbApiToken;
            Picasso.get().load(posterPath)
                    .placeholder(DrawableCompat.wrap(requireContext().getResources().getDrawable(
                            R.drawable.ic_image_black_24dp)))
                    .error(DrawableCompat.wrap(requireContext().getResources().getDrawable(
                            R.drawable.ic_broken_image_black_24dp)))
                    .into(moviePosterImageView);

            // Title
            movieSynopsisTextView.setText(movie.getOverview());

            // Rating
            float rating = getResources().getInteger(R.integer.no_of_rating_stars) * (
                    movie.getVoteAverage() / 10);
            voteAverageBar.setRating(rating);

            // Vote average
            voteAverageTextView.setText(String.valueOf(movie.getVoteAverage()));


            // Release date
            releaseDateTextView.setText(movie.getFormattedDate());

            if (mActionBar != null) {
                mActionBar.setTitle(movie.getTitle());
                movieTitleTextView.setText(movie.getTitle());
            }


            // Attach reviews adapter
            mReviewsAdapter = new ReviewsAdapter(requireContext());
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            reviewsRecyclerView.setAdapter(mReviewsAdapter);

            // Attach videos adapter
            mVideosAdapter = new VideosAdapter(null, requireContext());
            videosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            videosRecyclerView.setAdapter(mVideosAdapter);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                reviewsRecyclerView.setNestedScrollingEnabled(false);
                videosRecyclerView.setNestedScrollingEnabled(false);
            }

            // Favorite feature
            mImageView = v.findViewById(R.id.favoriteImageView);
            mImageView.setOnClickListener((view) -> addOrRemoveFromFavorites());

        } else {
            Log.w(TAG, "Movie in Details screen is null");
        }
    }

    private void addOrRemoveFromFavorites() {
        mMoviesDetailViewModel.addOrRemoveFromFavorites(requireContext(), mMovie);
    }

    private void hideProgressBar() {
        mMovieDetailsProgressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mMovieDetailsProgressBar.setVisibility(View.VISIBLE);
    }

    private void setFavoriteOn() {
        addFavorite();
        mImageView.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_star_black_24dp));
    }

    private void setFavoriteOff() {
        removeFavorite();
        mImageView.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
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

    @Override
    protected void onInternetConnectivityOff() {
    }

    @Override
    protected void onInternetConnectivityOn() {
        // Retry if initial load was done but otherwise fetch whole e.g. if on app start
        // device was disconnected but user established a connection
        List currentReviewsList = mMoviesDetailViewModel.getReviewsLiveData().getValue();
        if (currentReviewsList != null && currentReviewsList.size() > 0) {
            mMoviesDetailViewModel.retry();
        } else {
            mMoviesDetailViewModel.refreshReviewsList();
        }
    }

    void addFavorite() {
        AppExecutors.getInstance().diskIO().execute(() ->
                MoviesDatabase.getInstance(requireContext())
                        .favoriteMovieModel()
                        .addFavoriteMovie(mMovie));
    }

    void removeFavorite() {
        AppExecutors.getInstance().diskIO().execute(() ->
                MoviesDatabase.getInstance(requireContext())
                        .favoriteMovieModel()
                        .removeFavoriteMovie(mMovie));
    }
}
