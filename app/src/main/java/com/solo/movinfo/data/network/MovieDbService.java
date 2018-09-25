package com.solo.movinfo.data.network;

import com.solo.movinfo.data.network.responsemodels.MoviesResponse;
import com.solo.movinfo.data.network.responsemodels.ReviewsResponse;
import com.solo.movinfo.data.network.responsemodels.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("page") int page);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") String id, @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getVideos(@Path("id") String id);
}
