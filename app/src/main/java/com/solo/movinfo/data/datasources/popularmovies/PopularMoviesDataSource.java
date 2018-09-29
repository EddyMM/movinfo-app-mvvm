package com.solo.movinfo.data.datasources.popularmovies;


import android.support.annotation.NonNull;

import com.solo.movinfo.data.datasources.base.PagedMoviesDataSource;
import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.data.network.MovieDbApi;
import com.solo.movinfo.data.network.MovieDbService;
import com.solo.movinfo.data.network.responsemodels.MoviesResponse;
import com.solo.movinfo.utils.Constants;

import retrofit2.Call;


public class PopularMoviesDataSource extends PagedMoviesDataSource {


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
            @NonNull LoadInitialCallback<Integer, Movie> callback) {
        final int page = Constants.MOVIES_LIST_INITIAL_PAGE_NUMBER;

        MovieDbService mMovieDbService = MovieDbApi.getInstance();

        Call<MoviesResponse> call;
        call = mMovieDbService.getPopularMovies(page);

        makeApiCall(call, callback, page);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Movie> callback) {
        super.loadAfter(params, callback);
        final int page = params.key;

        MovieDbService mMovieDbService = MovieDbApi.getInstance();

        Call<MoviesResponse> call;
        call = mMovieDbService.getPopularMovies(page);
        makeApiCall(call, callback, page);
    }
}
