package com.solo.movinfo.data.model;


import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String iso_639_1;

    @SerializedName("iso_3166_1")
    private String iso_3166_1;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private String resolution;

    @SerializedName("type")
    private String type;

    @Override
    public String toString() {
        return String.format("Videos[name=%s]", this.name);
    }

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getResolution() {
        return resolution;
    }

    public String getType() {
        return type;
    }
}
