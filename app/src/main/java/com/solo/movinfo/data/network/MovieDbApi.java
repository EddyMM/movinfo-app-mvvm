package com.solo.movinfo.data.network;

import com.solo.movinfo.BuildConfig;
import com.solo.movinfo.utils.Constants;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDbApi {
    private MovieDbApi() {
    }

    public static MovieDbService getInstance(int page) {
        Retrofit retrofit;

        // Build a client with an interceptor to add the API key
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request initialRequest = chain.request();
                    HttpUrl initialHttpUrl = initialRequest.url();

                    HttpUrl modifiedHttpUrl = initialHttpUrl.newBuilder()
                            .addQueryParameter(Constants.API_KEY_REQUEST_KEY,
                                    BuildConfig.TheMovieDbApiToken)
                            .addQueryParameter(Constants.PAGE_KEY, String.valueOf(page))
                            .build();
                    Request modifiedRequest = initialRequest.newBuilder()
                            .url(modifiedHttpUrl)
                            .build();

                    return chain.proceed(modifiedRequest);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_DB_API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(MovieDbService.class);
    }
}
