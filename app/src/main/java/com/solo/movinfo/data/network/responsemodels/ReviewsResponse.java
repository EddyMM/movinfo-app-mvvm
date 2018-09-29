package com.solo.movinfo.data.network.responsemodels;


import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.model.Review;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> reviews;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public String getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
