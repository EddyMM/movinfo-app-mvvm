package com.solo.movinfo.data.model;


import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @Override
    public String toString() {
        return String.format("Review[Author=%s]", this.author);
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
