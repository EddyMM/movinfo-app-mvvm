package com.solo.movinfo.data.network.responsemodels;


import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.model.Review;

import java.util.List;

public class ReviewResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Review> mReviews;

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
        return mReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
