package com.solo.movinfo.data.network.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.model.Movie;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    private
    String page;
    @SerializedName("total_results")
    private
    String totalResults;
    @SerializedName("total_pages")
    private
    String totalPages;
    @SerializedName("results")
    private
    List<Movie> results;

    public MoviesResponse(String page, String totalResults, String totalPages,
            List<Movie> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }
}
